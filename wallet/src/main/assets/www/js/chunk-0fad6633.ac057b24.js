(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-0fad6633"],{1414:function(t,e,i){t.exports=i.p+"img/v41.191df5d7.png"},"356a":function(t,e,i){t.exports=i.p+"img/v32.55aeddab.png"},4679:function(t,e,i){"use strict";var n=i("da5a"),a=i.n(n);a.a},9365:function(t,e,i){"use strict";i.r(e);var n=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"game"},[i("div",{staticClass:"mine_top"},[i("div",{staticClass:"return",on:{click:function(e){return t.retreat()}}}),i("span",[t._v("庄园")])]),i("div",{staticClass:"game_cen"},[i("div",{staticClass:"game_bg"},[i("van-swipe",{staticStyle:{width:"100%",height:"100%"},attrs:{touchable:!0,"show-indicators":!1,"indicator-color":"white"}},[i("van-swipe-item",[i("div",{staticClass:"bg0"})]),i("van-swipe-item",[i("div",{staticClass:"bg1"})]),i("van-swipe-item",[i("div",{staticClass:"bg2"})])],1)],1),i("div",{staticClass:"grade"},t._l(t.arr,function(e,n){return i("div",{key:n},[i("div",[i("img",{attrs:{src:n===t.showIndex?e.Tab:e.levebtn,alt:""},on:{click:function(e){return t.Level(n,e)}}})])])}),0)])])},a=[],s=(i("ac6a"),{name:"",data:function(){return{arr:[{level:"1",levebtn:i("dbf5"),Tab:i("eecc")},{level:"2",levebtn:i("d76e"),Tab:i("b766")},{level:"3",levebtn:i("a787"),Tab:i("356a")},{level:"4",levebtn:i("1414"),Tab:i("cf23")}],level1:[],level2:[],level3:[],level4:[],showIndex:0}},created:function(){this.$store.dispatch("pig_num"),this.pig_num()},mounted:function(){},methods:{retreat:function(){this.$router.go(-1)},Level:function(t,e){this.showIndex=t},pig_num:function(){var t=this,e=JSON.parse(this.$store.state.pig_num);e.forEach(function(e){1===e.goodsid?t.level1.push(e):2===e.goodsid?t.level2.push(e):3===e.goodsid?t.level3.push(e):4===e.goodsid&&t.level4.push(e)})},getRandomNum:function(t,e){var i=[];if(1===e)return i.push(t),i;var n=t/e*2,a=1+~~(Math.random()*n-1);return i.push(a),i.concat(this.getRandomNum(t-a,--e))}},components:{}}),o=s,r=(i("4679"),i("2877")),c=Object(r["a"])(o,n,a,!1,null,"92a35a0c",null);e["default"]=c.exports},a787:function(t,e,i){t.exports=i.p+"img/v31.f301dd71.png"},ac6a:function(t,e,i){for(var n=i("cadf"),a=i("0d58"),s=i("2aba"),o=i("7726"),r=i("32e9"),c=i("84f2"),l=i("2b4c"),u=l("iterator"),d=l("toStringTag"),p=c.Array,v={CSSRuleList:!0,CSSStyleDeclaration:!1,CSSValueList:!1,ClientRectList:!1,DOMRectList:!1,DOMStringList:!1,DOMTokenList:!0,DataTransferItemList:!1,FileList:!1,HTMLAllCollection:!1,HTMLCollection:!1,HTMLFormElement:!1,HTMLSelectElement:!1,MediaList:!0,MimeTypeArray:!1,NamedNodeMap:!1,NodeList:!0,PaintRequestList:!1,Plugin:!1,PluginArray:!1,SVGLengthList:!1,SVGNumberList:!1,SVGPathSegList:!1,SVGPointList:!1,SVGStringList:!1,SVGTransformList:!1,SourceBufferList:!1,StyleSheetList:!0,TextTrackCueList:!1,TextTrackList:!1,TouchList:!1},f=a(v),g=0;g<f.length;g++){var m,h=f[g],L=v[h],b=o[h],S=b&&b.prototype;if(S&&(S[u]||r(S,u,p),S[d]||r(S,d,h),c[h]=p,L))for(m in n)S[m]||s(S,m,n[m],!0)}},b766:function(t,e,i){t.exports=i.p+"img/v22.5a6707ad.png"},cf23:function(t,e,i){t.exports=i.p+"img/v42.cdcb68eb.png"},d76e:function(t,e,i){t.exports=i.p+"img/v21.a19f426e.png"},da5a:function(t,e,i){},dbf5:function(t,e,i){t.exports=i.p+"img/v11.6e2596a1.png"},eecc:function(t,e,i){t.exports=i.p+"img/v12.fd36a64c.png"}}]);
//# sourceMappingURL=chunk-0fad6633.ac057b24.js.map