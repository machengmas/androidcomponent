# AndroidComponent
Android公用组件
（1）appupgrade（app升级组件）
检测更新接口：
请求参数（键值对的形式）：  checkUpdateParams.put("appKey", ApkUtils.getAppPackageName(context));
返回值要求的格式(json)：{
	"code" : "1",
	"data" : {
		"downloadUrl" : "http://...",
		"latestVersionCode" : 2,
		"latestVersionName" : "1.0",
		"minOSVersion" : "4.2.2",
		"size" : 1779020,
		"updateDate" : "2017-12-31"
	}
}
Android5.0及以上支持在无线网络下无感知下载最新的apk