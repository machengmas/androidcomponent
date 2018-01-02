# AndroidComponent
Android公用组件<br>
（1）appupgrade(app升级组件)<br>
检测更新接口格式:<br>
请求参数(键值对的形式):<br>
checkUpdateParams.put("appKey", ApkUtils.getAppPackageName(context));<br>
返回值要求的格式(json):<br>
{
	"code" : "1",
	"data" : {
		"downloadUrl" : "http://...",
		"latestVersionCode" : 2,
		"latestVersionName" : "1.0",
		"minOSVersion" : "4.2.2",
		"size" : 1779020,
		"updateDate" : "2017-12-31"
	}
}<br>
Android5.0及以上支持在无线网络下自动下载最新的apk<br>
gradle引入方式：<br>
Step 1. Add the JitPack repository to your build file<br>
Add it in your root build.gradle at the end of repositories:<br>
allprojects {<br>
	repositories {<br>
		...<br>
		maven { url 'https://jitpack.io' }<br>
	}<br>
}<br>
Step 2. Add the dependency<br>
dependencies {<br>
	compile 'com.github.machengmas:androidcomponent:1.0-beta1'<br>
}

