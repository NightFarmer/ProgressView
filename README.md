# ProgressView
## 简介
ProgressView是一个可以定制的镂空加载button，支持设置边框宽度，按钮颜色。

## 示例  
暂时无图

## 使用方法
### 添加依赖

gradle

/build.gradle
```
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```
/app/build.gradle
```
dependencies {
    ...
    compile 'com.github.NightFarmer:ProgressView:v1.0'
}
```
### 布局文件
```
    <com.nightfarmer.progressview.ProgressView
        android:id="@+id/progressView"
        android:layout_width="100dp"
        android:layout_height="50dp" />
```

### 代码设置(kotlin)
```
    //设置按钮主色
    progressView.color = ContextCompat.getColor(this, R.color.yourColor)
    //设置按钮边框宽度
    progressView.borderWidth = 5f
    //设置按钮圆角半径
    progressView.radius = 20f
    //设置按钮初始化进度
    progressView.progress = 0f
    //设置按钮文字尺寸
    progressView.textSize = 50f

    progressView.onStart = {
        //点击开始回调
    }
    progressView.onPause = {
        //点击暂停回调
    }
    progressView.onResume = {
        //点击继续回调
    }
    progressView.onOpen = {
        //点击打开回调
    }

    //设置按钮状态
    progressView.state = ProgressView.ProgressState.Loading
    progressView.state = ProgressView.ProgressState.Paused
    progressView.state = ProgressView.ProgressState.Finished
    progressView.state = ProgressView.ProgressState.IDLE

    //自定义状态文字
    progressView.idleText = "下载"
    progressView.loadingTextRender = { progress: Float -> "${(progress * 100).toInt()}%" }
    progressView.pausedText = "暂停"
    progressView.finishedText = "完成"

```