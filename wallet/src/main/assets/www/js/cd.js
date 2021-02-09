//cordova 的返回事件和 hbuilder的不一样 没有返回值貌似...如果你增加了backbutton 那就把以前的给覆盖了, 如果你这里什么也不干 它就退不了了.....
document.addEventListener("deviceready", function(){
  //两次后退关闭 toast功能依赖cordova-plugin-x-toast
  var canQuit = false;
  function addBackEvent(){
    //两次退出
    document.addEventListener("backbutton", function(){
      if(location.hash != "" && location.hash != "#/" && location.hash != "#/home" && location.hash != "#/login"){
        history.back();
        return;
      }
      if(canQuit == false){
        canQuit = true;
        window.plugins.toast.showShortBottom("再按一次退出");
        setTimeout(function(){
          canQuit = false;
        }, 2000);  
      }
      else{
         navigator.app.exitApp(); 
      }    
    }, false);
  }
  addBackEvent();  
}, false);

document.addEventListener('deviceready', () => {
  let chcp = window.chcp;
  setTimeout(function() {
    // 检测更新
    chcp.fetchUpdate((error, data) => {
      if (error) {
        console.log('--更新版本异常，或其他错误--', error.code, error.description);
        if (error.code === -2) {

          //应用商店升级方式
          // var dialogMessage = '有新的版本是否下载';
          // //调用升级提示框 点击确认会跳转对应商店升级
          // chcp.requestApplicationUpdate(dialogMessage, null, null);

          //打开浏览器页面升级方式
          navigator.notification.confirm(
            '检测到新的版本,是否升级?', // message
             function(idx){
               if(idx == 1){
                 cordova.InAppBrowser.open('http://192.168.28.167/', '_system');
               }
             },            // callback to invoke with index of button pressed
            '提示',           // title
            ['确定','取消']     // buttonLabels
        );
        }
      }
      // 服务器版本信息
      // console.log('--更新的版本信息--', data.config);
      // 版本信息
      chcp.getVersionInfo((err, data) => {
        console.log('服务器应用时间版本: ' + data.readyToInstallWebVersion);
        console.log('当前应用时间版本： ' + data.currentWebVersion);
        console.log('当前应用version name: ' + data.appVersion);
      });
    })
  }, 3000);
;
});