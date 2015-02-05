# CycleView

Projeto da disciplina de "Projetão" do Centro de Informática da UFPE.

Para compilar o projeto, são necessários o [Android SDK](https://developer.android.com/sdk/index.html#Other), o [Android NDK **9d**](http://lmgtfy.com/?q=android+ndk+9d+windows+download) e o [GStreamer SDK for Android](http://docs.gstreamer.com/display/GstSDK/Installing+for+Android+development).

Para receber um streaming de vídeo compatível, é necessário prover um streaming do tipo:
```shell
$ gst-launch-1.0 -v v4l2src ! videoconvert ! jpegenc ! tcpserversink host=192.168.42.1 port=5000 sync=false
```
