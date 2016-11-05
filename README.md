# djplayer
这是一个音乐播放器APP，仿照“天天动听”音乐播放器v7版来实现，目前只支持本地音乐的播放（暂不支持网络下载功能）

# 已实现的主要功能
## 音乐扫描
动态扫描手机媒体库里面的歌曲信息，并添加到自己的数据库里面，扫描功能可过滤指定时长及大小的歌曲文件。 

## 歌曲列表
支持歌曲分类显示，主要包括：
- 所有的歌曲。
- 显示“我喜欢”的歌曲。
- 显示“最近播放”的歌曲。
- 按照专辑分类显示。
- 按照歌手分类显示。
- 按照文件路径分类i显示。

## 播放控制以及信息显示
- 可以控制：播放/暂停、前一曲、下一曲、播放模式（顺序播放、单曲循环、列表循环）。
- 播放信息：歌曲名字、歌手名字、歌手图片、播放进度、歌词，由于没有网络资源，歌手图片以及歌词需要手动添加在指定的路径下面。

## 歌词文件、歌手图片保存路径
- 歌词文件：手机内存/djplayer/lyric/
- 歌手图片：手机内存/djplayer/artistPicuture   

# 一些模块的截图
主界面、歌曲列表、音乐扫描分别如下图所示

![主界面](https://raw.githubusercontent.com/JaffarOu/djplayer/master/PictureInReadMe/main.jpg)
![歌曲列表](https://raw.githubusercontent.com/JaffarOu/djplayer/master/PictureInReadMe/songList.jpg)
![音乐扫描](https://raw.githubusercontent.com/JaffarOu/djplayer/master/PictureInReadMe/scanSong.jpg)
