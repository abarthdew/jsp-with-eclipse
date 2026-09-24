## 20. 서블릿 필터(Servlet Filter)

- 톰캣(서버)는 사용자 요청이 들어오면 적절한 소프트웨어를 실행해서 응답함

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/73.png)

### 🔰 무언가 옵션을 해야 할 때(예시 : 언어설정)

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/74.png)

- 방법 1 : 서블릿 문서 내 req.setCharaterEncoding 처리.
    - 그러나, 설정을 servlet 마다 하는 것이 번거로움
- 방법 2 : 톰캣 서버 설정을 바꿈
    - 하지만, 톰캣은 여러 어플리케이션을 서비스하는 하나의 컨테이너므로, 설정을 함부로 바꿀 수 없음
- 해결 ⇒ 필터 사용

### 🔰 필터

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/75.png)

- 중간에 갈아끼울 수 있는 소프트웨어
- 중간에서 가로채기 가능
    - 필터 먼저 실행 후 서블릿 실행
    - 필터가 서블릿을 실행할지 말지 결정도 함
    - 인증, 권한 관리 가능
    - 기본 설정 가능
    - `서블릿으로 가기 전 요청`, `서블릿을 거치고 나온 응답` 모두 가로채기 가능
- 필터 interface add

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/76.png)

- 필터 설정 방법
    1. web.xml
    
    ```html
    <!-- 필터 설정 -->
    <filter>
      <filter-name>characterEncodingFilter</filter-name>
      <filter-class>com.newlecture.web.filter.Filter</filter-class>
    </filter>
      <filter-mapping>
      <filter-name>characterEncodingFilter</filter-name>
      <url-pattern>/*</url-pattern> <!-- 모든 url : 어떤 요청이 어든 동작 -->
    </filter-mapping>
    ```
    
    2. annotation
    
    ```html
    @WebFilter("/*")
    public class Filter implements javax.servlet.Filter {
    ```
## 23. 여러 개의 Submit 버튼 사용하기

```html
<form action="calc" method="post">
  <div>
    <label>x : </label>
    <input type="text" name="x">
  </div>
  <div>
    <label>y : </label>
    <input type="text" name="y">
  </div>
  <div>
    <p>여러 개의 버튼 구분</p>
    <input type="submit" name="button" value="덧셈">
    <input type="submit" name="button" value="뺄셈">
  </div>
</form>
```

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/77.png)

- button : 덧셈
    
    ![raw data](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/78.png)
    _raw data_
    

```java
protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
  res.setCharacterEncoding("UTF-8");
  res.setContentType("text/html; charset=UTF-8");
  
  String x_ = req.getParameter("x");
  String y_ = req.getParameter("y");
  String button = req.getParameter("button");
  
  int x = x_ != null && !x_.equals("") ? Integer.parseInt(x_) : 0;
  int y = y_ != null && !y_.equals("") ? Integer.parseInt(y_) : 0;
  
  int result = button.equals("add") ? x + y : x - y;
  
  res.getWriter().printf("result is %d\n", result);
    
}
```

## 24. 입력 데이터 배열로 받기

- 같은 이름으로 여러개의 데이터 받기

```html
<form action="data-array" method="post">
  <div>
    <label>같은 name값으로 배열로 보낼 수 있음 : </label>
    <input type="text" name="num"><br>
    <input type="text" name="num"><br>
    <input type="text" name="num"><br>
    <input type="text" name="num"><br>
    <input type="text" name="num">
  </div>
  <div>
    <input type="submit" name="button" value="add">
  </div>
</form>
```

```java
protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
  res.setCharacterEncoding("UTF-8");
  res.setContentType("text/html; charset=UTF-8");
  
  // 동일한 이름으로 여러개가 한꺼번에 오기 때문에
  String[] num = req.getParameterValues("num");
  
  int result = 0;
  for (String n : num) {
    result += Integer.parseInt(n);
  }
  
  res.getWriter().printf("result is %d\n", result);
    
}
```

## 25. 상태 유지의 필요성

- 전역 변수처럼 서블릿들 사이에서 값을 유지하기 위한 것이 필요하게 됨

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/79.png)

- 어플리케이션 객체, session, cookie에서 데이터를 어딘가 보관하게 됨
- 세 가지 도구를 사용하는 방법

## 26. Application 객체와 그것을 사용한 상태 값 저장

- 어플리케이션 저장소를 사용하는 방법에 대해 알아보자
- 1, 2, 3, 4는 서블릿
- 요청이 일어나면 일을 하고난 후 메모리에서 사라짐.

![GIF.gif](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/80.gif)

- 그래서, 1번이 처리했던 내용을 2번이 받아서 처리할 수 없음.

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/81.png)

- 이를 위해 서블릿 컨텍스트(문맥)라는 저장소를 사용함.
- 서블릿들이 서로간의 문맥을 이어갈수록 하는 저장소.
- 1 서블릿이 컨텍스트에 데이터를 저장하면 2 서블릿이 자원을 공유받을 수 있음. → 어플리케이션 저장소라고도 함.

## 27. Session 객체로 상태 값 저장하기(그리고 Application 객체와의 차이점)

- 어플리케이션 객체 : 어플리케이션 전역에서 쓸 수 있음
- 세션 객체 : 세션 범주 내에서 쓸 수 있음
    - 세션 : 현재 접속한 사용자
    - 즉, 사용자 별로 그 공간이 달라질 수 있음

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/82.png)

- 크롬 브라우저는 1개의 프로세스 하위에 여러 개의 스레드를 둠
- 즉, 여러 개를 띄워도 단일 프로세스에 스레드만 여러 개

## 28. 웹 서버가 현재 사용자(Session)을 구분하는 방식

- 어플리케이션 공간 : 사용자 요청을 수반한 프로그램 처리 중, 다른 서블릿에게 공유하고 싶은 내용을 저장하는 공간
- 세션 공간 : 사용자마다 공유 데이터를 저장하는 공간. 개인별에 맞는 번호 등으로 구분되어 있음.
    - 사용자 요청이 처음 왔을 때는 세션이 존재하지 않음(응답을 보낼 때 was에서 세션 아이디를 부여)
        
        ![초기 요청의 경우, 어플리케이션 공간에는 접근 가능하지만 세션은 안 됨](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/83.png)
        
        초기 요청의 경우, 어플리케이션 공간에는 접근 가능하지만 세션은 안 됨
        
    - 기존 사용자(세션 아이디를 가진)의 요청에 비로소 세션을 쓸 수 있음
        
        ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/84.png)
        

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/85.png)

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/86.png)

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/87.png)

## 29. Cookie를 이용해 상태값 유지하기

- 쿠키 : 사물함(서버)에 보관하지 않고 클라이언트가 가지고 다닐 수 있는 값
- 클라이언트가 가지고 다니며 꺼내 쓰거나 가져와서 쓸 수 있으며, 그 값이 유지됨

### 🔰 요약

- 서버에 저장할 수 있는 저장소 : 어플리케이션, 세션 저장소
- 클라이언트에 저장할 수 있는 저장소 : 쿠키 저장소

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/88.png)

### 요청

- 브라우저가 설정
    - header 설정 - getHeader("remote-host")
    - 쿠키가 있으면 가져감 - getCookies(); (쿠키 꺼내기)
- 클라이언트 데이터 : 사용자 데이터 - getParameter("x")

### 응답

- 브라우저
    - 사용자에게 쿠키를 심어 보낼 수 있음 - addCookie(); (쿠키 심기)

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/89.png)

```java
// 쿠키 배열을 돌면서 찾기
int x = 0;
for(Cookie c : cookies) {
  if (c.getName().equals("value")) {
    x = Integer.parseInt(c.getValue());	
    break;
  }
}  
int   y = v;
//   쿠키 배열을 돌면서 찾기
Stri  ng buttonOperator = "";
for(  Cookie c : cookies) {
  if (c.getName().equals("button")) {
    buttonOperator = c.getValue();	
    break;
  }
}
result = buttonOperator.equals("+") ? x + y : x - y;
res.getWriter().printf("result is %d\n", result);
```

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/90.png)

## 30. Cookie의 path 옵션

- 서블릿은 하나가 아닌 여러개를 만듬
- 서블릿마다 데이터를 쿠키로 저장한다면? → 서블릿마다 쿠키가 다름
- 쿠키 url 설정할 수 있음 → 해당 범주 안에 있는 서블릿에만 가져올 수 있음
- 그래야만 이름이 충돌나거나 값의 공유가 비효율적이지 않을 수 있음
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/91.png)
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/92.png)
    
- path를 /add로 설정했을 때
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/93.png)
    
    - 66, +를 클릭했으나실체 쿠키값은 저장되지 않음
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/94.png)
    
    - /add로 주소를 추가하면, 오류는 나지만 쿠키값은 66, +가 되었음을 확인할 수 있음.

## 31. Cookie의 maxAge 옵션

- 쿠키는 브라우저, 클라이언트에 저장하는 데이터로서, 브라우저에 요청을 할 때 항상 가져오는 데이터

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/95.png)

- 쿠키는 maxAge가 없으면 브라우저가 닫힐 때 쿠키도 없어짐
- 즉, maxAge가 설정되어 있으면 브라우저가 닫혀도 쿠키는 maxAge 설정값만큼 유지됨

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/96.png)

- 보통 쿠키는 브라우저 메모리에 있다가, maxAge 설정이 되면 외부 파일에 설정이 저장됨
- 설정 후 테스트 해보면 5초 후(설정값) value값이 사라지는 것을 확인할 수 있음

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/97.png)

## 32-33. Application / Session / Cookie의 차이점 정리

- Application 저장소
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/98.png)
    
    - WAS 와 생명주기가 동일
- Session 저장소
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/99.png)
    
    - 현재 사용자 대상
    - 서버 자원 사용
- Cookie 저장소
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/100.png)
    
    - 서버가 아닌 웹 브라우저에 저장
    - 특정 url에 대해서만 사용 가능하도록 분류 가능
    - 오래 보관해야 할 데이터 저장 가능

## 33. 서버에서 페이지 전환해주기(redirect)

- 기존 방식 : 숫자 입력 → 더하기/빼기 클릭 → `백지화면` → 뒤로가서 다시 숫자 입력 → =버튼 입력

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/101.png)

- 서버에서 요청을 받아 처리 후 calc.html 페이지를 돌려주는 방식

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/102.png)

## 34. 동적인 페이지(서버 페이지)의 필요성

### <정적>

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/103.png)

- 단순히 페이지를 redirect해서 보여주는 게 아닌, 3이라는 값이 떠 있는 문서를 동적으로 보내줘야 함.

### <동적>

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/104.png)

- /calcPage라는 문서에 3이라는 동적 값을 박아서 응답.

## 35. 처음이자 마지막으로 동적인 페이지 서블릿으로 직접 만들기

- 코드 참조

## 36. 계산기 서블릿 구현하기

- 코드 참조

## 37. 쿠키 삭제하기

- 단순히 exp = "";이렇게 하면 쿠키가 삭제되지 않음
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/105.png)
    

```jsx
if (button != null && button.equals("C")) { // 쿠키 삭제
  expCookie.setMaxAge(0);
}
```

## 38. GET / POST에 특화된 서비스 함수

- 부모가 가진 service는 doGet, doPost를 구분해서 호출하도록 되어 있음

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/106.png)

### 1. 구분해서 사용하지 않으려면 super.service()를 지우고 사용

```jsx
@Override
protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
  if (req.getMethod().equals("GET")) { // get 요청만 처리(대문자 필수)
    System.out.println("GET 요청이 왔습니다.");
  } else if (req.getMethod().equals("POST")) { // post 요청만 처리
    System.out.println("POST 요청이 왔습니다.");
  }
}
```

### 2. GET, POST에 따라 둘 중 하나를 Override 해서 사용

```jsx
super.service(req, res);
```

- doGet, doPost가 override되어있지 않으면 오류남
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/107.png)
    
- 즉, 아래의 경우 `System.out.println("GET 요청이 왔습니다.");`이 콘솔창에 찍힌 후, `super.service(req, res);` 부분에서 doGet 메서드가 없어서 오류남.

```jsx
<form action="service2" method="get">
  <input type="submit" value="요청">
</form>
```

- 그러나, 만약 form method=post일 경우 doPost가 있기 때문에 이 경우는 가능.

```jsx
@Override
protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    
  if (req.getMethod().equals("GET")) { // get 요청만 처리(대문자 필수)
    System.out.println("GET 요청이 왔습니다.");
  } else if (req.getMethod().equals("POST")) { // post 요청만 처리
    System.out.println("POST 요청이 왔습니다.");
  }
  super.service(req, res);
}

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse res) {
  System.out.println("doPost 메소드가 호출되었습니다.");
}
```

### 3. 공통 코드를 지우고 doGet, doPost 메서드만 남기는 방식으로 할 수 있음. (어차피 서비스 함수를 통해서 호출되므로)

```jsx
<form action="service3" method="get">
  <input type="submit" value="요청">
</form>
<form action="service3" method="post">
  <input type="submit" value="요청">
</form>
```

```jsx
@Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) {
    System.out.println("doGet 메소드가 호출되었습니다.");
  }
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) {
    System.out.println("doPost 메소드가 호출되었습니다.");
  }
```

## 39. 계산기 프로그램 하나의 서블릿으로 합치기

- calc, calcPage 따로 만들 필요가 없음

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/108.png)

- 코드 참조

## 40. JSP를 이용한 자바 웹 프로그래밍

- JSP라는 알바생을 둔다고 생각해보자
- html 형태 페이지를 out.write()로 만드려면?
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/109.png)
    
- 확장자만 .jsp로 붙임 → 아래 html 페이지에 out.write()를 붙여 출력할 수 있는 형태로 바꿈.
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/110.png)
    
- 이 html파일을 언제 서블릿 코드로 바꿔줄까? → jsp가 요구될 때, 즉 사용자가 jsp 확장자를 가진 페이지를 요청할 때 이 파일을 servlet으로 만들어줌
- 또한, url 매핑은 jsp 파일명 그대로 매핑됨
- jsp 파일은 해당 공간에 만들어짐
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/111.png)
    
- 코드 참조

```jsx
<tr>
  <td colspan="4">${3+4}</td> // string이 아닌 계산 결과가 나옴
</tr>
```

### 파일 디렉토리 구조

- 브라우저에서 여기를 보는 게 아님. 여기는 홈 디렉토리가 아님. 서비스되고 있는 파일들이 아님. 개발할 때 사용되는 개발 디렉토리일 뿐.
    
    → 프로젝트를 실행하면 배포하게 되는데, 이때 톰캣의 홈 디렉토리로 옮겨지게 됨.
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/112.png)
    
- 하지만, 톰캣은 여러 서비스를 운영할 수 있기에 실제 톰캣의 워크 디렉토리가 아닌 이클립스가 관리하는 서비스 운영을 위한 별도의 사본을 만들게 됨.
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/113.png)
    
    - server path : 배포 디렉토리(실제 서비스되는 배포 디렉토리). 즉, 개발 시 사용되는 임시 디렉토리라고 할 수 있음.
    - C:\Users\auswo\Eclipse\jee-2021-09\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\work\Catalina\localhost\ROOT\org\apache\jsp
    - 톰캣 디렉토리는 아니고, 개발할 때 쓰는 디렉토리
    - 카탈리나 : 톰캣의 예전 프로젝트 이름
    - 이 디렉토리 안에 만들어진 .java 파일을 열어보면 jsp가 만들어낸 out.write()로 이루어진 파일을 볼 수 있음.
        
        ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/114.png)
        
- 아래 코드를 jsp로 만들게 되면?
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/115.png)
    
    - out.write(int x = 3;);, out.write(int y = 4;); 이렇게 되어버림
    
    ![제대로 코드를 나오게 하려면 이렇게 <%%>를 삽입](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/116.png)
    _제대로 코드를 나오게 하려면 이렇게 <%%>를 삽입_
    
### 예제
    
```jsx
// calculator.jsp
<!DOCTYPE html>
<%
int x = 3;
int y = 4;
%>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
    <form action="add.jsp" method="get">
    <div>
        <ul>
        <li><label for="x">x : </label><input   name="x"></li>
        <li><label for="y">y : </label><input   name="y"></li>
        </ul>
        <div><input type="submit" value="sum"></div>
    </div>
    </form>
</body>
</html>
```

```jsx
// C:\Users\auswo\Eclipse\jee-2021-09\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\work\Catalina\localhost\ROOT\org\apache\jsp
// calculator_jsp.java
out.write("<!DOCTYPE html>\r\n");

int x = 3; // <%%>로 삽입한 코드가 이렇게 해석됨
int y = 4;

out.write("\r\n");
out.write("<html>\r\n");
out.write("<head>\r\n");
out.write("<meta charset=\"UTF-8\">\r\n");
out.write("<title>Insert title here</title>\r\n");
out.write("</head>\r\n");
out.write("<body>\r\n");
out.write("	<form action=\"add.jsp\" method=\"get\">\r\n");
out.write("		<div>\r\n");
out.write("			<ul>\r\n");
```
    
## 41. JSP의 코드 블록

- jsp는 파일에 쓰인 모든 것을 out.write()로 출력해달라고 인식함.
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/117.png)
    
- <%%>와 같은 자바 코드 블록을 넣어야 코드 형태로 인식함.

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/118.png)

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/119.png)

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/120.png)

- out.write는 문자열 출력
- out.print는 정수, 실수, 문자, 문자열 등 좀 더 다양한 자료형 출력

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/121.png)

- 이 코드는 구문에러 : 메서드에 들어가기 때문
- 해결 : 느낌표를 써서 멤버변수 영역에 들어갈 수 있음
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/123.png)
    
- 해당 페이지에 따른 인코딩 방식, 컨텐트 타입 지시
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/124.png)
    
    - 이 지시 블록은 왜 필요할까? : 브라우저에 알려주기 위해
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/125.png)
    
    - 초기에는 이런 식으로 초기 설정을 했음
    
    ![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/126.png)
    
    - 하지만, 이 방법은 이미 출력이 진행된 다음 설정은 잘못됐다는 에러가 뜸.
    - <%%> 코드가 servlet의 메서드부에 가장 먼저 들어갈 거라는 보장이 없음. 즉, 하단 <html>부터 out.write()하기 전에 <%%>가 진행되어야 하는데, 그러지 못할 수도 있음.
    - 때문에, 어떠한 코드보다 앞어 진행되어야 하는 코드를 <%@%> 지시블럭을 써서 표현함.

## 42. JSP의 내장객체 간단히 알아보기

- 아래 코드는 int page = 1 에서 중복되었다는 에러가 남

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/127.png)

- JSP에서 만들어낸 서블릿에서 이미 int page라는 변수가 있음.

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/128.png)

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/129.png)

- `pageContext` : 페이지 내에서만 임시로 데이터를 저장할 수 있음.
- `out` : 출력 도구
- `page` : 페이지의 객체를 참조

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/130.png)

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/131.png)

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/132.png)

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/133.png)

- 내장 객체 : JSP가 만들어낸 서블릿 안에 미리 선언된 변수 → 이를 코드 블럭에서 사용할 수 있음.

## 43. JSP로 만드는 Hello 서블릿

```jsx
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
    
<%
  String cnt_ = request.getParameter("cnt"); // request : 내장객체
  int cnt = 100;
  if (cnt_ != null && !cnt_.equals("")) {
    cnt = Integer.parseInt(cnt_);
  }
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
  <% for (int i=0; i<cnt; i++) { %>
      안녕하세요 <br>
  <% } %>
  // 코드블럭 내 코드는 jsp가 만들기 때문에 {} 필수 
</body>
</html>
```

![Untitled](https://raw.githubusercontent.com/abarthdew/jsp-with-eclipse/main/images/134.png)

## 44. 스파게티 코드를 만드는 JSP

- 코드 참조