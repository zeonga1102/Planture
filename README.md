![image](https://user-images.githubusercontent.com/71905164/186938244-f719605f-b1fc-47e1-9a33-ed1e93b2230e.png)
# 🌻Planture
카메라로 사진을 찍거나 앨범에 있는 사진을 선택하면 식물을 인식하여 해당하는 정보를 보여주고 기르는 식물에 대한 정보를 등록해서 관리에 도움을 주는 서비스를 포함한 식물을 주제로 하는 SNS 안드로이드 어플리케이션

# 🌼Intro
* 카메라로 사진을 찍어나 앨범에서 선택한 사진에서 식물을 감지하고 크롤링한 정보 표시
* 기르는 식물을 등록하고 물 주는 주기 등 정보를 기록해서 식물 관리에 도움
* 사진과 글을 공유하는 SNS 기능
* **개발 인원(3명)**: 김수연, 노예원, 이정아
* [프로젝트 설명 및 시연 영상 Youtube](https://www.youtube.com/watch?v=tAnNuym4Raw)

# 🌷Project
### 사용 기술
* Java
* Tensorflow Lite
* Firebase
* Jsoup

### Architecture
![image](https://user-images.githubusercontent.com/71905164/186938580-2c3cc942-6eb0-411b-a593-654e9cabf895.png)

### 맡은 부분
<details>
<summary>CommunityFragment <a href="https://github.com/zeonga1102/Planture/blob/master/app/src/main/java/org/tensorflow/lite/examples/classification/view/community/CommunityFragment.java">📑코드</a></summary>

사용자들끼리 사진과 글을 공유하는 SNS 기능입니다.<br>
커뮤니티에 포함되는 기능 전체를 구현했습니다. 사용자들의 게시글 조회와 작성 및 수정, 그리고 좋아요 기능입니다.
</details>
<details>
<summary>LoginActivity <a href="https://github.com/zeonga1102/Planture/blob/master/app/src/main/java/org/tensorflow/lite/examples/classification/view/login/LoginActivity.java">📑코드</a></summary>

Firebase Auth를 사용해서 로그인을 합니다.
</details>
<details>
<summary>SignupActivity <a href="https://github.com/zeonga1102/Planture/blob/master/app/src/main/java/org/tensorflow/lite/examples/classification/view/login/SignUpActivity.java">📑코드</a></summary>

Firebase Auth를 사용해서 회원가입을 하고 사용자의 정보를 Firebase DB에 저장합니다.
</details>
<details>
<summary>ResultActivity <a href="https://github.com/zeonga1102/Planture/blob/master/app/src/main/java/org/tensorflow/lite/examples/classification/view/ResultActivity.java">📑코드</a></summary>

촬영하거나 선택한 사진에서 인식된 식물의 정보를 크롤링하여 나타냅니다.<br>
크롤링 코드는 다른 팀원이 작성하였습니다.
</details>
<details>
<summary>HomeFragment <a href="https://github.com/zeonga1102/Planture/blob/master/app/src/main/java/org/tensorflow/lite/examples/classification/view/HomeFragment.java">📑코드</a></summary>

메인 화면입니다. 선택한 검색 방식에 따른 동작을 구현했습니다.<br>
'촬영해서 검색'을 누르면 ClassifierActivity로 이동하고 '저장된 사진으로 검색'을 누르면 사진에서 모델을 이용해 식물을 인식한 뒤 인식 결과와 함께 ResultActivity로 이동합니다.
</details>
