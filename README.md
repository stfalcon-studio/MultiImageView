#MultiImageView
Library for display a few images in one MultiImageView like avatar of group chat

![alt tag](http://i.imgur.com/S4QoKVB.gif)

### Download

Download via Gradle:
```gradle
compile 'com.github.stfalcon:multiimageview:0.1'
```

or Maven:
```xml
<dependency>
  <groupId>com.github.stfalcon</groupId>
  <artifactId>multiimageview</artifactId>
  <version>0.1</version>
  <type>pom</type>
</dependency>
```

### Usage
Add MultiImageView to layout xml file
```xml
<com.stfalcon.multiimageview.MultiImageView
        android:id="@+id/iv"
        android:layout_width="100dp"
        android:layout_height="100dp"/>
```
In java class find view by id
```java
final MultiImageView multiImageView = (MultiImageView) findViewById(R.id.iv);
```
For adding image to MultiImageView use method addImage(Bitmap bitmap). For exapple:
```java
multiImageView.addImage(BitmapFactory.decodeResource(getResources(), R.drawable.avatar1));
```
For setting shape of MultiImageView use method setShape(MultiImageView.Shape shape).
```java
multiImageView.setShape(MultiImageView.Shape.RECTANGLE);//Rectangle with round corners
multiImageView.setShape(MultiImageView.Shape.CIRCLE);//Circle
multiImageView.setShape(MultiImageView.Shape.NONE);//Without shape
```
If you choose rectangle shape you can set corner radious
```java
multiImageView.setRectCorners(50);
```
To clear MultiImageView use:
```java
multiImageView.clear();
```


Take a look at [Sample projects] [sample] for more information

### License 

Copyright 2016 stfalcon.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.



[sample]: <https://github.com/stfalcon-studio/MultiImageView/tree/master/sample>
