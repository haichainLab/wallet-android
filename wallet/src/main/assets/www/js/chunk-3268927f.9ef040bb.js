(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-3268927f"],{"0b97":function(t,e,n){"use strict";var o=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"c-pay-box"},[n("div",{staticClass:"c-pay-num"},[t._v(t._s(t._f("toNumberString")(t.num))+" XToken")]),n("van-password-input",{attrs:{value:t.value}}),n("van-number-keyboard",{staticClass:"c-pay-keyboard",attrs:{show:t.showKeyboard},on:{input:t.onInput,delete:t.onDelete}})],1)},i=[],r=(n("c5f6"),{name:"pay",props:{num:{type:Number,default:0}},data:function(){return{value:"",showKeyboard:!0}},methods:{onInput:function(t){if(this.value=(this.value+t).slice(0,6),this.value.length>=6){var e=this.value;this.$emit("finish",e),this.value=""}},onDelete:function(){this.value=this.value.slice(0,this.value.length-1)}},mounted:function(){}}),a=r,c=(n("791b"),n("2877")),s=Object(c["a"])(a,o,i,!1,null,"0c6244c4",null);e["a"]=s.exports},"0e41":function(t,e,n){"use strict";var o=n("e8d9"),i=n.n(o);i.a},"11e9":function(t,e,n){var o=n("52a7"),i=n("4630"),r=n("6821"),a=n("6a99"),c=n("69a8"),s=n("c69a"),u=Object.getOwnPropertyDescriptor;e.f=n("9e1e")?u:function(t,e){if(t=r(t),e=a(e,!0),s)try{return u(t,e)}catch(n){}if(c(t,e))return i(!o.f.call(t,e),t[e])}},"28a5":function(t,e,n){"use strict";var o=n("aae3"),i=n("cb7c"),r=n("ebd6"),a=n("0390"),c=n("9def"),s=n("5f1b"),u=n("520a"),l=n("79e5"),f=Math.min,p=[].push,d="split",h="length",v="lastIndex",g=4294967295,b=!l(function(){RegExp(g,"y")});n("214f")("split",2,function(t,e,n,l){var m;return m="c"=="abbc"[d](/(b)*/)[1]||4!="test"[d](/(?:)/,-1)[h]||2!="ab"[d](/(?:ab)*/)[h]||4!="."[d](/(.?)(.?)/)[h]||"."[d](/()()/)[h]>1||""[d](/.?/)[h]?function(t,e){var i=String(this);if(void 0===t&&0===e)return[];if(!o(t))return n.call(i,t,e);var r,a,c,s=[],l=(t.ignoreCase?"i":"")+(t.multiline?"m":"")+(t.unicode?"u":"")+(t.sticky?"y":""),f=0,d=void 0===e?g:e>>>0,b=new RegExp(t.source,l+"g");while(r=u.call(b,i)){if(a=b[v],a>f&&(s.push(i.slice(f,r.index)),r[h]>1&&r.index<i[h]&&p.apply(s,r.slice(1)),c=r[0][h],f=a,s[h]>=d))break;b[v]===r.index&&b[v]++}return f===i[h]?!c&&b.test("")||s.push(""):s.push(i.slice(f)),s[h]>d?s.slice(0,d):s}:"0"[d](void 0,0)[h]?function(t,e){return void 0===t&&0===e?[]:n.call(this,t,e)}:n,[function(n,o){var i=t(this),r=void 0==n?void 0:n[e];return void 0!==r?r.call(n,i,o):m.call(String(i),n,o)},function(t,e){var o=l(m,t,this,e,m!==n);if(o.done)return o.value;var u=i(t),p=String(this),d=r(u,RegExp),h=u.unicode,v=(u.ignoreCase?"i":"")+(u.multiline?"m":"")+(u.unicode?"u":"")+(b?"y":"g"),y=new d(b?u:"^(?:"+u.source+")",v),x=void 0===e?g:e>>>0;if(0===x)return[];if(0===p.length)return null===s(y,p)?[p]:[];var _=0,w=0,S=[];while(w<p.length){y.lastIndex=b?w:0;var E,k=s(y,b?p:p.slice(w));if(null===k||(E=f(c(y.lastIndex+(b?0:w)),p.length))===_)w=a(p,w,h);else{if(S.push(p.slice(_,w)),S.length===x)return S;for(var I=1;I<=k.length-1;I++)if(S.push(k[I]),S.length===x)return S;w=_=E}}return S.push(p.slice(_)),S}]})},"325c":function(t,e,n){"use strict";n("a481");var o=n("e814"),i=n.n(o),r=(n("28a5"),n("6b54"),{getTime:function(t){function e(t){return t=t.toString(),t[1]?t:"0"+t}t=t||new Date;var n=t.getFullYear(),o=t.getMonth()+1,i=t.getDate(),r=t.getHours(),a=t.getMinutes(),c=t.getSeconds();return[e(n),e(o),e(i)].join("-")+" "+[e(r),e(a),e(c)].join(":")},getDate:function(t){return this.getTime(t).split(" ")[0]},addDays:function(t,e){return this.addTimes(t,60,60,24,e)},addHours:function(t,e){return this.addTimes(t,60,60,e,1)},addMinutes:function(t,e){return this.addTimes(t,60,e,1,1)},addSeconds:function(t,e){return this.addTimes(t,e,1,1,1)},addTimes:function(t,e,n,o,i){t=t||new Date;var r=t.getTime(),a=r+1e3*e*n*o*i,c=new Date(a);return c},formatTime:function(t,e){function n(t){return t=i()(t),t<10?"0"+t:t}function o(t){return t=i()(t),t>12?t-12:t}t||(t=new Date);var r=t.getFullYear(),a=n(t.getMonth()+1),c=n(t.getDate()),s=n(t.getHours()),u=n(o(t.getHours())),l=n(t.getMinutes()),f=n(t.getSeconds());return e?(e=e.replace(/yyyy/g,r),e=e.replace(/MM/g,a),e=e.replace(/dd/g,c),e=e.replace(/HH/g,s),e=e.replace(/hh/g,u),e=e.replace(/mm/g,l),e=e.replace(/ss/g,f),e):""+r+a+c+s+l+f},getQueryString:function(t){var e="",n=window.location.href,o=-1,i=t.length;do{if(o=n.indexOf(t+"="),-1!=o){if("?"==n.charAt(o-1)||"&"==n.charAt(o-1)){n=n.substr(o);break}n=n.substr(o+i+1)}}while(-1!=o);if(-1!=o){var r=n.indexOf("&");e=-1==r?n.substr(i+1):n.substring(i+1,r)}return e},getErrorCode:function(t){return i()(t.toString().split("__")[1])},getErrorDesc:function(t){return t.toString().split("__")[2]}});e["a"]=r},3846:function(t,e,n){n("9e1e")&&"g"!=/./g.flags&&n("86cc").f(RegExp.prototype,"flags",{configurable:!0,get:n("0bfb")})},"5d6b":function(t,e,n){var o=n("e53d").parseInt,i=n("a1ce").trim,r=n("e692"),a=/^[-+]?0[xX]/;t.exports=8!==o(r+"08")||22!==o(r+"0x16")?function(t,e){var n=i(String(t),3);return o(n,e>>>0||(a.test(n)?16:10))}:o},"5dbc":function(t,e,n){var o=n("d3f4"),i=n("8b97").set;t.exports=function(t,e,n){var r,a=e.constructor;return a!==n&&"function"==typeof a&&(r=a.prototype)!==n.prototype&&o(r)&&i&&i(t,r),t}},"6b54":function(t,e,n){"use strict";n("3846");var o=n("cb7c"),i=n("0bfb"),r=n("9e1e"),a="toString",c=/./[a],s=function(t){n("2aba")(RegExp.prototype,a,t,!0)};n("79e5")(function(){return"/a/b"!=c.call({source:"a",flags:"b"})})?s(function(){var t=o(this);return"/".concat(t.source,"/","flags"in t?t.flags:!r&&t instanceof RegExp?i.call(t):void 0)}):c.name!=a&&s(function(){return c.call(this)})},7445:function(t,e,n){var o=n("63b6"),i=n("5d6b");o(o.G+o.F*(parseInt!=i),{parseInt:i})},"791b":function(t,e,n){"use strict";var o=n("c01f"),i=n.n(o);i.a},"8b97":function(t,e,n){var o=n("d3f4"),i=n("cb7c"),r=function(t,e){if(i(t),!o(e)&&null!==e)throw TypeError(e+": can't set as prototype!")};t.exports={set:Object.setPrototypeOf||("__proto__"in{}?function(t,e,o){try{o=n("9b43")(Function.call,n("11e9").f(Object.prototype,"__proto__").set,2),o(t,[]),e=!(t instanceof Array)}catch(i){e=!0}return function(t,n){return r(t,n),e?t.__proto__=n:o(t,n),t}}({},!1):void 0),check:r}},9093:function(t,e,n){var o=n("ce10"),i=n("e11e").concat("length","prototype");e.f=Object.getOwnPropertyNames||function(t){return o(t,i)}},a1ce:function(t,e,n){var o=n("63b6"),i=n("25eb"),r=n("294c"),a=n("e692"),c="["+a+"]",s="​",u=RegExp("^"+c+c+"*"),l=RegExp(c+c+"*$"),f=function(t,e,n){var i={},c=r(function(){return!!a[t]()||s[t]()!=s}),u=i[t]=c?e(p):a[t];n&&(i[n]=u),o(o.P+o.F*c,"String",i)},p=f.trim=function(t,e){return t=String(i(t)),1&e&&(t=t.replace(u,"")),2&e&&(t=t.replace(l,"")),t};t.exports=f},aa77:function(t,e,n){var o=n("5ca1"),i=n("be13"),r=n("79e5"),a=n("fdef"),c="["+a+"]",s="​",u=RegExp("^"+c+c+"*"),l=RegExp(c+c+"*$"),f=function(t,e,n){var i={},c=r(function(){return!!a[t]()||s[t]()!=s}),u=i[t]=c?e(p):a[t];n&&(i[n]=u),o(o.P+o.F*c,"String",i)},p=f.trim=function(t,e){return t=String(i(t)),1&e&&(t=t.replace(u,"")),2&e&&(t=t.replace(l,"")),t};t.exports=f},aae3:function(t,e,n){var o=n("d3f4"),i=n("2d95"),r=n("2b4c")("match");t.exports=function(t){var e;return o(t)&&(void 0!==(e=t[r])?!!e:"RegExp"==i(t))}},ae17:function(t,e,n){},b9e9:function(t,e,n){n("7445"),t.exports=n("584a").parseInt},c01f:function(t,e,n){},c5f6:function(t,e,n){"use strict";var o=n("7726"),i=n("69a8"),r=n("2d95"),a=n("5dbc"),c=n("6a99"),s=n("79e5"),u=n("9093").f,l=n("11e9").f,f=n("86cc").f,p=n("aa77").trim,d="Number",h=o[d],v=h,g=h.prototype,b=r(n("2aeb")(g))==d,m="trim"in String.prototype,y=function(t){var e=c(t,!1);if("string"==typeof e&&e.length>2){e=m?e.trim():p(e,3);var n,o,i,r=e.charCodeAt(0);if(43===r||45===r){if(n=e.charCodeAt(2),88===n||120===n)return NaN}else if(48===r){switch(e.charCodeAt(1)){case 66:case 98:o=2,i=49;break;case 79:case 111:o=8,i=55;break;default:return+e}for(var a,s=e.slice(2),u=0,l=s.length;u<l;u++)if(a=s.charCodeAt(u),a<48||a>i)return NaN;return parseInt(s,o)}}return+e};if(!h(" 0o1")||!h("0b1")||h("+0x1")){h=function(t){var e=arguments.length<1?0:t,n=this;return n instanceof h&&(b?s(function(){g.valueOf.call(n)}):r(n)!=d)?a(new v(y(e)),n,h):y(e)};for(var x,_=n("9e1e")?u(v):"MAX_VALUE,MIN_VALUE,NaN,NEGATIVE_INFINITY,POSITIVE_INFINITY,EPSILON,isFinite,isInteger,isNaN,isSafeInteger,MAX_SAFE_INTEGER,MIN_SAFE_INTEGER,parseFloat,parseInt,isInteger".split(","),w=0;_.length>w;w++)i(v,x=_[w])&&!i(h,x)&&f(h,x,l(v,x));h.prototype=g,g.constructor=h,n("2aba")(o,d,h)}},c961:function(t,e,n){"use strict";n.r(e);var o=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"home c-content"},[n("van-nav-bar",{staticClass:"c-nav-bar",attrs:{"left-text":"",title:"TEST页面"},on:{"click-left":function(e){return t.$router.back()}}}),n("van-row",{staticClass:"c-idx-header"},[n("van-col",{attrs:{span:"12"}},[n("a",{staticClass:"router-link-exact-active"},[t._v("aaa")])]),n("van-col",{attrs:{span:"12"}},[n("router-link",{attrs:{to:"/shop"}},[t._v("bbb")])],1)],1),n("button",{on:{click:t.test1}},[t._v("fsdfsd")]),n("button",{on:{click:t.test2}},[t._v("tst2")]),n("button",{on:{click:t.test3}},[t._v("code")]),n("input",{directives:[{name:"model",rawName:"v-model",value:t.text,expression:"text"}],attrs:{type:"text"},domProps:{value:t.text},on:{input:function(e){e.target.composing||(t.text=e.target.value)}}}),n("button",{on:{click:t.test4}},[t._v("login")]),n("button",{on:{click:t.test5}},[t._v("设置支付密码")]),n("button",{on:{click:function(e){return t.test6()}}},[t._v("设置钱")]),n("button",{on:{click:function(e){return t.test6(1)}}},[t._v("设置钱1")]),n("button",{on:{click:t.test7}},[t._v("清空测试数据")]),n("button",{on:{click:t.test8}},[t._v("支付")]),t._m(0),n("van-tabbar",{model:{value:t.active,callback:function(e){t.active=e},expression:"active"}},[n("van-tabbar-item",{attrs:{icon:"home-o",to:"/"}},[t._v("首页")]),n("van-tabbar-item",{attrs:{icon:"friends-o",to:"/setting"}},[t._v("个人中心")])],1),n("PayXToken",{model:{value:t.payShow,callback:function(e){t.payShow=e},expression:"payShow"}})],1)},i=[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"c-idx-farmbox c-flexbox"},[n("span",{staticClass:"c-idx-nofarm"},[t._v("当前未拥有农场,前去"),n("b",[t._v("商城")]),t._v("购买吧")])])}],r=(n("ae17"),n("bc3a"),function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("van-actionsheet",{attrs:{title:"请输入密码"},on:{cancel:t.onCanelPay},model:{value:t.pShow,callback:function(e){t.pShow=e},expression:"pShow"}},[n("Pay",{on:{finish:t.onPwdFinish}})],1)}),a=[],c=n("0b97"),s=n("325c"),u={name:"payActionsheet",components:{Pay:c["a"]},model:{prop:"payShow",event:"evtShow"},props:{payShow:!1,goods:[],finishUrl:{type:String,default:"/shop/payresult"}},watch:{payShow:function(t,e){this.pShow=t},pShow:function(t,e){this.$emit("evtShow",t)}},data:function(){return{pShow:!1}},methods:{onCanelPay:function(){this.$emit("cancel")},onPwdFinish:function(t){var e=this;this.goods?this.$http.post("order/confirmCart",{did:"device id"}).then(function(t){e.finishUrl?e.$router.push(e.finishUrl):e.$emit("success")}).catch(function(t){var n=s["a"].getErrorCode(t);n==window.globalData.code.PAYPASSWORD_IS_WRONG?e.$dialog.alert({message:"密码错误"}):n==window.globalData.code.CART_COIN_NOT_ENOUGH&&e.$dialog.confirm({message:"xtoken余额不足,是否去充值?"}).then(function(){}).catch(function(){})}):this.$dialog.alert({message:"商品数量为空"})}},mounted:function(){}},l=u,f=n("2877"),p=Object(f["a"])(l,r,a,!1,null,"2d61837c",null),d=p.exports,h="15822217182",v={name:"home",components:{PayXToken:d},data:function(){return{active:0,text:"",url:n("cf05"),payShow:!1}},methods:{test1:function(){this.payShow=!0},test2:function(){this.$http.post("/reqLogin",{appid:11}).then(function(t){console.log(t)})},test3:function(){var t=this,e="http://134.175.239.143:9501/guard/vcode";this.$http.post(e,{cmd:"1",body:{phone:h,did:"1",appid:11,op:"register"}}).then(function(e){t.res=e,console.log(t.res)})},test4:function(){var t="/reqCode";this.$http.post(t,{cmd:"1",body:{phone:h,did:"1",appid:11,op:"register",sid:this.res.data.body.sid,code:this.text}})},test5:function(){this.$http.post("/order/setTestPaypasswd",{did:"device id",paypasswd:123456}).then(function(t){console.log(t)})},test6:function(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:1e4;this.$http.post("/order/setMoney",{did:"device id",money:t}).then(function(t){console.log(t)})},test7:function(){this.$http.post("/order/emptyGoodsData",{did:"device id"}).then(function(t){console.log(t)})},test8:function(){this.$http.post("/order/confirmUnPayOrder",{orderno:"2019032157090",did:"device id"}).then(function(t){console.log(t)})}}},g=v,b=(n("0e41"),Object(f["a"])(g,o,i,!1,null,null,null));e["default"]=b.exports},cf05:function(t,e,n){t.exports=n.p+"img/logo.82b9c7a5.png"},e692:function(t,e){t.exports="\t\n\v\f\r   ᠎             　\u2028\u2029\ufeff"},e814:function(t,e,n){t.exports=n("b9e9")},e8d9:function(t,e,n){},fdef:function(t,e){t.exports="\t\n\v\f\r   ᠎             　\u2028\u2029\ufeff"}}]);
//# sourceMappingURL=chunk-3268927f.9ef040bb.js.map