# Copyright 2024 Paion Data. All rights reserved.
import logging
import os
import time
from functools import wraps

import requests
from flasgger import Swagger
from flask import Flask
from flask import request
from flask_cors import CORS


def random_filename(original_filename: str):
    """
    将一个文件名哈希成一个随机字符串，并用当前时间戳进行盐化，防止同名文件名的哈希冲突。

    :param original_filename: 带了后缀的文件名

    :return: 一个仅包含数字的哈希值
    """
    return str(hash((original_filename, time.time())))


def with_uploaded_file(f):
    """
    抽取 HTTP 请求中的上传文件，存储到一个随机路径下，执行 web 服务接口逻辑，并在逻辑执行完毕（不管报错与否）之后，
    删除本地文件

    :param f:  接口执行逻辑

    :return: 封装了上述步骤的 higher-order web 服务接口执行逻辑
    """

    @wraps(f)
    def decorated_function(*args, **kwargs):
        uploaded_file = request.files['audio']
        file_path = random_filename(uploaded_file.filename)
        uploaded_file.save(file_path)
        kwargs["audio_path"] = file_path

        try:
            return f(*args, **kwargs)
        finally:
            os.remove(file_path)

    return decorated_function


app = Flask(__name__)

CORS(app)

gunicorn_error_logger = logging.getLogger('gunicorn.error')
app.logger.handlers.extend(gunicorn_error_logger.handlers)
app.logger.setLevel(logging.DEBUG)

app.config['SWAGGER'] = {
    'title': '派昂科技 AI 自动语音识别 (Automatic Speech Recognition) 测试 API',
    'openapi': '3.0.2'
}
Swagger(app)


def __remote_inference(remote_url: str, audio_path: str):
    """
    调用远程 AI 进行语音转文字 API 进行转译。

    :param remote_url:  模型接口地址
    :param audio_path:  本地语音文件路径

    :return: 转译文字
    """
    import requests

    url = remote_url

    payload = {}
    files = [
        ('audio', ('file', open(audio_path, 'rb'), 'application/octet-stream'))
    ]
    headers = {
        'accept': '*/*'
    }

    response = requests.request("POST", url, headers=headers, data=payload, files=files)

    return response.text


def __local_inference(audio_path: str):
    """
    调用本地模型进行转译。

    :param audio_path:  本地语音文件路径

    :return: 转译文字
    """
    import wave
    import sys
    import json

    from vosk import Model, KaldiRecognizer

    wf = wave.open(audio_path, "rb")
    if wf.getnchannels() != 1 or wf.getsampwidth() != 2 or wf.getcomptype() != "NONE":
        print("音频格式目前只支持 WAV (mono PCM)")
        sys.exit(1)

    model = Model(lang="cn")

    rec = KaldiRecognizer(model, wf.getframerate())
    rec.SetWords(True)
    rec.SetPartialWords(True)

    while True:
        data = wf.readframes(4000)
        if len(data) == 0:
            break
        if rec.AcceptWaveform(data):
            print(rec.Result())
        else:
            print(rec.PartialResult())

    return json.loads(rec.FinalResult())["text"]


def __remote_is_healthy():
    """
    探测远程 AI 模型当前是否能接收转译请求

    :return: 如果可以接收请求，则返回 True，否则返回 False
    """
    import requests

    return requests.get("https://asr-test.paion-data.dev/healthcheck").status_code == 200


@app.route("/healthcheck")
def healthcheck():
    """
    Docker 容器健康检查

    :return: 如果正常运行，返回 200
    """
    return "Success", 200


@app.route("/transcribe", methods=['POST'])
def transcribe():
    audio_file_id = request.json["audioFileId"]

    response = requests.get("http://web-oss:8081?fileId=" + audio_file_id, stream=True)

    with open('audio.wav', 'wb') as f:
        for chunk in response.iter_content(chunk_size=16 * 1024):
            f.write(chunk)
    f.close()

    transcribedText = __local_inference()

    






@app.route("/model1", methods=["POST"])
@with_uploaded_file
def model1(**kwargs):
    """
    使用 1 号基础模型将一段音频转译成文字格式。

    支持多种音频格式，包括 .mp3, .wav

    如果 1 号模型的远程服务器不可用，则会自动切换到本地模式，用一个未经过训练的中文模型进行转译
    ---
    requestBody:
      content:
        multipart/form-data:
          schema:
            type: object
            properties:
              audio:
                type: string
                format: binary
    responses:
      200:
        description: Success
    """
    if __remote_is_healthy():
        return __remote_inference("https://asr-test.paion-data.dev/model1", kwargs["audio_path"])
    else:
        return __local_inference(kwargs["audio_path"])


@app.route("/model2", methods=["POST"])
@with_uploaded_file
def model2(**kwargs):
    """
    使用 2 号基础模型将一段音频转译成文字格式。

    仅支持 .wav 格式

    如果 2 号模型的远程服务器不可用，则会自动切换到本地模式，用一个未经过训练的中文模型进行转译
    ---
    requestBody:
      content:
        multipart/form-data:
          schema:
            type: object
            properties:
              audio:
                type: string
                format: binary
    responses:
      200:
        description: Success
    """
    if __remote_is_healthy():
        return __remote_inference("https://asr-test.paion-data.dev/model2", kwargs["audio_path"])
    else:
        return __local_inference(kwargs["audio_path"])
