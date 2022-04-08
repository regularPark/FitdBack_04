### 22.04.08

2번째 데모에서도 오류가 나서, 3번째 데모를 만들기로 결정.

- initial commit 

- classpath 'com.google.gms:google-services:4.3.10' 를 추가하자 ,
```
A problem occurred evaluating project ':app'.
> ASCII
```
에러 발생 

-> gradle plugin 3.5.1 / gradle 5.4.1 / kotlin 1.3.10으로 올린 후 빌드 성공


- implementation 'com.google.firebase:firebase-auth-ktx' 추가하자
```
Duplicate class android.support.v4.app.INotificationSideChannel found in modules classes.jar (androidx.core:core:1.2.0) and classes.jar (com.android.support:support-compat:27.1.1)
Duplicate class android.support.v4.app.INotificationSideChannel$Stub found in modules classes.jar (androidx.core:core:1.2.0) and classes.jar (com.android.support:support-compat:27.1.1)
Duplicate class android.support.v4.app.INotificationSideChannel$Stub$Proxy found in modules classes.jar (androidx.core:core:1.2.0) and classes.jar (com.android.support:support-compat:27.1.1)
Duplicate class android.support.v4.os.IResultReceiver found in modules classes.jar (androidx.core:core:1.2.0) and classes.jar (com.android.support:support-compat:27.1.1)
Duplicate class android.support.v4.os.IResultReceiver$Stub found in modules classes.jar (androidx.core:core:1.2.0) and classes.jar (com.android.support:support-compat:27.1.1)
Duplicate class android.support.v4.os.IResultReceiver$Stub$Proxy found in modules classes.jar (androidx.core:core:1.2.0) and classes.jar (com.android.support:support-compat:27.1.1)
Duplicate class android.support.v4.os.ResultReceiver found in modules classes.jar (androidx.core:core:1.2.0) and classes.jar (com.android.support:support-compat:27.1.1)
Duplicate class android.support.v4.os.ResultReceiver$1 found in modules classes.jar (androidx.core:core:1.2.0) and classes.jar (com.android.support:support-compat:27.1.1)
Duplicate class android.support.v4.os.ResultReceiver$MyResultReceiver found in modules classes.jar (androidx.core:core:1.2.0) and classes.jar (com.android.support:support-compat:27.1.1)
Duplicate class android.support.v4.os.ResultReceiver$MyRunnable found in modules classes.jar (androidx.core:core:1.2.0) and classes.jar (com.android.support:support-compat:27.1.1)

Go to the documentation to learn how to Fix dependency resolution errors.
```


에러 발생 

- androidx migration 이전 이후 auth.ktx 활성화, 아래 에러 발생

```
e: C:/Users/shine/.gradle/caches/transforms-2/files-2.1/440244369116d816b91e99eaa87dad4d/firebase-auth-ktx-21.0.2/jars/classes.jar!/META-INF/java.com.google.android.libraries.firebase.firebase_auth_ktx_granule.kotlin_module: Module was compiled with an incompatible version of Kotlin. The binary version of its metadata is 1.5.1, expected version is 1.1.13.
```




