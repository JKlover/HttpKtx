# HttpKtx
DSL网络请求

##这个网络请求是用协程库写的，请使用Androidx库,最好在kotlin中使用,因为用kt的内联函数封装的，引用lifecycle自动感知activity生命周期，activity销毁自动取消协程

网络请求库
使用
添加依赖


```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```	
	
	
	
```
dependencies {
	        implementation 'com.github.JKlover:HttpKtx:v1.0.0'
	}
```
	


  
  
  	
  
  在注意因为要没有内存泄漏，Activity必须继是FragmentActivity的子类,在任何的地方调用都可以,Activity一被回收请求自动释放资源
  
  请求方式如下:
  模拟网页搜索引擎的请求:
  
  
  ```
  httpHtmlEngine {
            url = "http://aqdyen.com/search.asp"
            method = "post"
            enc="gb2312"
//            headers{
//                "Connection"- "Keep-Alive"
//                "Referer"-"http://aqdyen.com/"
//                "User-Agent"-"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.162 Safari/537.36 Edg/80.0.361.109"
//            }
            params{
                "searchword"-"路西法"
            }
            onStart {
                 Log.e("ST--->拿到数据了", "开始请求")
            }
            onSuccess {
                 println(it)
            }
        }
```

   
  
  其中enc是网页的编码
  这样就能获取到网页搜索里的内容，再用jsoup爬取内容
  
  json请求
```
  httpJsonEngine<ResponseEntity> {
            url="https://api.apiopen.top/getSingleJoke?sid=28654780"
	    method="get"
            onStart{

            }
            onSuccess {
              Log.e("ST--->httpJsonEngine",it.toString())
            }
            onFail {

            }
        }
 ``` 

  

