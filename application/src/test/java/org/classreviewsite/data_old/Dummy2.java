//package org.classreviewsite.data_old;
//
//import org.classreviewsite.lecture.service.ProfessorService;
//import org.classreviewsite.review.dto.Request.ClassReviewRequest;
//import org.classreviewsite.review.service.ReviewService;
//import org.classreviewsite.user.dto.CreateUserRequest;
//import org.classreviewsite.user.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class Dummy2 {
//
//    @Autowired
//    ReviewService reviewService;
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    ProfessorService professorService;
//
//    @Test
//    void 교수_이미지변경(){
//        professorService.updateProfile(1L, "문미경");
//        professorService.updateProfile(9L, "김대한");
//    }
//
//    @Test
//    void 테스트계정_회원가입(){
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user01")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240001)
//                        .userType("STUDENT")
//                        .build()
//        );
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user02")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240002)
//                        .userType("STUDENT")
//                        .build()
//        );
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user03")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240003)
//                        .userType("STUDENT")
//                        .build()
//        );
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user04")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240004)
//                        .userType("STUDENT")
//                        .build()
//        );
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user05")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240005)
//                        .userType("STUDENT")
//                        .build()
//        );
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user06")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240006)
//                        .userType("STUDENT")
//                        .build()
//        );
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user07")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240007)
//                        .userType("STUDENT")
//                        .build()
//        );
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user08")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240008)
//                        .userType("STUDENT")
//                        .build()
//        );
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user09")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240009)
//                        .userType("STUDENT")
//                        .build()
//        );
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user10")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240010)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user11")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240011)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user12")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240012)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user13")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240013)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user14")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240014)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user15")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240015)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user16")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240016)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user17")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240017)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user18")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240018)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user19")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240019)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user20")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240020)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user21")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240021)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user22")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240022)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user23")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240023)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user24")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240024)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user25")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240025)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user26")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240026)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user27")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240027)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user28")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240028)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user29")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240029)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user30")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240030)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user31")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240031)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user32")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240032)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user33")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240033)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user34")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240034)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user35")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240035)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user36")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240036)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user37")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240037)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user38")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240038)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user39")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240039)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user40")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240040)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user41")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240041)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user42")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240042)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user43")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240043)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user44")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240044)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user45")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240045)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user46")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240046)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user47")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240047)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user48")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240048)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user49")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240049)
//                        .userType("STUDENT")
//                        .build()
//        );
//
//        userService.signUp(
//                CreateUserRequest.builder()
//                        .department("소프트웨어학과")
//                        .nickname("user50")
//                        .password("1234")
//                        .userName("김개발")
//                        .userNumber(20240050)
//                        .userType("STUDENT")
//                        .build()
//        );
//    }
//
//    //    @Transactional
//    @Test
//    void 대한민국해커톤_데이터(){
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("첫 해커톤", "처음 참여한 해커톤 대회라서 미흡한 점도 많았지만 그만큼 배운점도 많았다.", 20240001L, 4.0, "대한민국 해커톤", 1L, 1L , 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("해커톤.^^", "함께 밤새니까 힘이나고 정말 즐겁다", 20240002L, 3.0, "대한민국 해커톤", 1L, 1L , 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("해커톤", "친구들과 함께 프로젝트를완성해 나가는 과정이 유익했다.", 20240003L, 4.0, "대한민국 해커톤", 1L, 1L , 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("열정", "열정이 중요하다", 20240004L, 5.0, "대한민국 해커톤", 1L, 1L , 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("무박3일", "피곤하다. 시간이 의외로 넉넉하다. 다들 대단하신것 같다.", 20240005L, 4.0, "대한민국 해커톤", 1L, 1L , 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("해커톤 리뷰", "먹을것이 풍족한 것에 비해 네트워크(와이파이)가 너무 준비가 미흡한것 같다.", 20240006L, 4.0, "대한민국 해커톤", 1L, 1L , 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("감옥...?", "밥 시간이 정해진대로 잘 나와서 좋았지만 메뉴는 아쉬움ㅠ", 20240007L, 4.0, "대한민국 해커톤", 0L, 1L , 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("좋은 경험", "흔치않은 좋은기회에 참여해 잊지못할 추억을 만들었다.", 20240008L, 4.0, "대한민국 해커톤", 0L, 1L , 1L)
//        );
//
//
//
//    }
//
//    @Test
//    void K_ICT전시회_데이터(){
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("부산 수업리뷰 공유 시스템 리뷰", "에브리타임 외에 강의과목에 대한 리뷰 작성 아이디어가 되게 좋았고, 홈페이지 디자인도 귀여웠습니다.", 20240011L, 5.0, "K-ICT WEEK in BUSAN", 0L, 1L , 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("수업리뷰", "정말정말정말 재미있었어요!!!", 20240012L, 5.0, "K-ICT WEEK in BUSAN", 0L, 1L , 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("리뷰작성", "욕설 방지 필터링이나리 너무 신기하다.", 20240013L, 5.0, "K-ICT WEEK in BUSAN", 0L, 1L , 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("울마고 화이팅", "다양한 대학교의 수업을 같이 들을 수 있는 것도 좋은 방법인것 같습니다.", 20240014L, 5.0, "K-ICT WEEK in BUSAN", 0L, 1L , 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("대동고등학교 후기", "고등학생 입장에서 어쩌면 이해하기 힘든 내용을 친절하게 잘 설명해서 좋았고 아이디어와 그런면에서 충분히 이후에 도움이 될 것이라고 생각합니다", 20240015L, 5.0, "K-ICT WEEK in BUSAN", 0L, 1L , 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("좋아요", "나오면 많이 쓸 것 같은 느낌!!", 20240050L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("안녕하세요", "감사합니다", 20240016L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("동서대학교 체험", "선생님께서 잘 설명해주시고 흥미로운 내용 위주여서 재밌었다.", 20240017L, 5.0, "K-ICT WEEK in BUSAN", 1L, 1L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("좋아요", "처음에 별 기대 안했는데 둘러보니까 생각보다 좋아서 놀랐어요!", 20240018L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("다양한 기술이 많습니다!", "생각의 틀을 깨주는 기술이 많네요", 20240019L, 4.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("리뷰" , "재미있었다", 20240020L, 5.0, "K-ICT WEEK in BUSAN", 0L, 1L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("도움이 많이 될 것 같음", "유용함", 20240021L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("인공지능", "다양한 강의자료입니다.", 20240022L, 5.0, "K-ICT WEEK in BUSAN", 0L , 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("부산 수업리뷰 공유시스템 만드신거 멋져용", "부산 수업리뷰 공유시스템 만드신거 멋져용", 20240023L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("주제 다양성", "최근 기술을 보여주고 있으며, 다양한 주제에 대해 전시되어 있어 좋았습니다.", 20240024L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("K-ict 박람회", "동서대학교 화이팅입니다:)", 20240025L, 5.0, "K-ICT WEEK in BUSAN", 1L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("안동대 방문 후기 .....", "우리 성건이도 쉽게 이해할수 있을 정도로 설명을 친절하게 잘 해주셨습니다!",20240026L ,5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L )
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("신기한게 많네요 ㅎㅎ", "열심히해 ㅎㅎ", 20240027L, 5.0, "K-ICT WEEK in BUSAN", 0L , 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("동서대학교 ubse", "화이팅", 20240028L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("대진고 후기", "미래에 대학에 굉장히 도움이 되는 프로그램 이였습니다!", 20240029L, 5.0, "K-ICT WEEK in BUSAN", 0L, 1L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("녀뮤죻흔푸로구램이예용.", "촴...류익훼요...", 20240030L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("박람회 후기", "여러가지 새로운 경험을 하게 되었습니다", 20240031L, 5.0, "K-ICT WEEK in BUSAN", 1L ,0L, 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("공부 너무 재밌어요","진심을 담았습니다", 20240032L, 4.0, "K-ICT WEEK in BUSAN", 0L, 1L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of(" 전시회"," k ict 전시회에서 동서대학교 설명 상세히 들을 수 있어 좋았습니다", 20240033L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("인공지능","학생들이 개발한 다양한 프로그램들을 알 수 있어 유익한 시간이였습니다.", 20240034L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L ,0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("부기공산 학생들 왔다감","경품추천이 많은게 너무 좋다", 20240035L, 5.0, "K-ICT WEEK in BUSAN", 0L, 1L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("특별한 시간","모르던 정보들을 많이 알 수 있었던 시간이었다.", 20240036L, 5.0, "K-ICT WEEK in BUSAN", 1L, 1L , 0L)
//
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("대학생 꿀팁", "대학생들이 전공별로 자신에게 맞는 수업을 찾을수 있을것같다", 20240037L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("동서대학교 소프트웨어 최고","직접 개발한 프로그램 성공하시길", 20240038L, 5.0, "K-ICT WEEK in BUSAN", 1L , 1L, 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("정다은", "재밌어요", 20240039L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("소프트웨어학과", "너무 유익해요!!", 20240040L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("동서대학교 부스 체험후기", "생각보다 볼 거리가 많아서 즐거웠고, 새로운 정보들을 접할 수 있어서 너무 유익하고 뜻깊은 하루였던 것 같습니다. 너무 좋아용 ~~!", 20240041L, 5.0, "K-ICT WEEK in BUSAN", 0L, 0L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("너무 좋은 강의입니다", "잘 만들었습니다", 20240042L, 4.0, "K-ICT WEEK in BUSAN", 1L, 0L, 1L)
//        );
//
//    }
//
//    @Test
//    void 소프트웨어공학_더미데이터(){
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("학점은 잘주나 얻어가는건 본인 하기나름", "시험이 쉽다, 수업 자료로 본인이 쓴 책을 쓰시는데 수업중 졸지만 않는다면 책에 있는 예제에서 시험 문제를 다 집어주기 때문에 시험 난이도가 한결 낮아지는 느낌이다 절평이라 본인이 아예 손을 놓은게 아니라면 학점도 후하게 주기 때문에 잘받아갈듯", 20240001L, 4.0, "소프트웨어공학", 1L, 0L, 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("코딩 할줄 모르시면 성실히 수업들으시면 성적 잘 나옵니다. 코딩 할줄아시면 출첵만 하시면 됩니다", "절대평가고 교수님께서 성적을 잘 주시려 노력하십니다.", 20240002L, 3.0, "소프트웨어공학", 1L, 1L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("열심히 하면 하는대로 돌아오는 수업입니다.과제 정말 중요해요 매주 과제해가는게 좀 힘들긴한데그거 잘하고 시험기간에 책한번 쫙 보시고 예제 손코딩 공부 좀 하시면 괜찮을거에요!과제 관리..잘하세요", "PPT로 강의식 수업하시고 4시간연강인데 2시간정도는 이론 나머지 시간은 실습시간입니다.실습시간에는 조교님께서 돌아다니시면서 질문받아주고 혼자 컴퓨터로 해보는 시간인데 보통 예제코드를 공부하거나 그때 과제를 합니다.거의 매주 과제가 나오는데 책에 있는 연습문제를 풀어가는 게 과제이고 후반부가면 가끔 PPT에 나오는 연습문제 2개정도가 과제로 나옵니다.", 20240003L, 5.0, "소프트웨어공학", 1L, 1L, 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("정말 학점 잘주십니다! 꼭 들으셔용. 그리고 과제 꼭 내셔야합니다!!", "뭔가 도중도중에 장난도 쳐주시고 나름 재밌게 진행해주셔요.개념 많이 내시기 때문에 개념위주로 공부하시고 책에 있던 문제 그대로 나올때 많습니다.\n", 20240004L, 3.0, "소프트웨어공학", 1L, 1L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("학점은 잘주나 얻어가는건 본인 하기나름", "시험이 쉽다, 수업 자료로 본인이 쓴 책을 쓰시는데 수업중 졸지만 않는다면 책에 있는 예제에서 시험 문제를 다 집어주기 때문에 시험 난이도가 한결 낮아지는 느낌이다 절평이라 본인이 아예 손을 놓은게 아니라면 학점도 후하게 주기 때문에 잘받아갈듯", 20240005L, 5.0, "소프트웨어공학", 0L, 0L, 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("공부해보면 별로 어렵진않은데 문제로 만나면 까다롭습니다. A4반장 분량의 치팅페이퍼가 허용되니까 필요한 공식이나 풀이법등을 적어둬서 유용하게 사용하세요.", "복습을 20분이상해주셔서 좋았습니다. 교수님이 직접만든 교안을 이용해 수업을 진행하십니다. 수업에 나온 내용을 바탕으로 시험문제가 나오니까 잘 들어야하고 시험 전에 기출문제를 주지만 매번 새롭게 문제를 만들어 내시니 어떻게 문제를 만드는지만 보세요.\n", 20240006L, 2.0, "소프트웨어공학", 1L, 1L, 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("C언어를 미리알고 수업듣는걸 추천합니다 아니면 수업시간에 잘못따라가실거에요", "수업을 열심히는 하십니다. 시험족보 즉 작년문제를 시험전에 나눠주시는데 비슷한유형의문제가  출제됩니다. 예습을안한생태로는 잘 따라가기 어려운수업입니다\n", 20240007L, 3.0, "소프트웨어공학", 1L, 1L, 0L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("기초를 쌓은 뒤 듣는다면 더 유익할 수업입니다.", "기초를 쌓은 뒤 듣는다면 더 유익할 수업입니다. C에 대해 잘 모르는 상태에서 중간고사를 보게되서 좀 빡세게 공부를 했었습니다. 근데 하다보니 기초가 잡혀서 기말고사는 중간고사 보다는 쉽게 갈 수 있었습니다. 덕분에 학점도 괜찮게 나왔구요. 정말 아이러니한데...ㅋㅋㅋ 강의가 어려워서 더 노력하게 되어... 성격이 잘 나오는.. 이상하게도 만족도는 높게 주고 싶은 강의 입니다. 강의는 심화과정이라 버거운 편이긴 합니다.", 20240008L, 4.0, "소프트웨어공학", 0L, 0L, 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("수업 필기 열심히하고 연습문제 많이 풀어보면 됩니다....", "잘 가르치십니다. 필기위주로 수업하시고 문제도 같이 풀어나가는 방식입니다연습문제만 많이 풀어도 될 것 같습니다. 수업만 잘 들어도 성적 잘나옵니다 연습문제만 많이 풀어도 될 것 같습니다. 수업만 잘 들어도 성적 잘나옵니다", 20240009L, 4.0, "소프트웨어공학", 1L, 1L , 1L)
//        );
//
//        reviewService.addReviewPost(
//                ClassReviewRequest.of("수업 꼭 열심히 듣고 따라가면서 이해해야 시험 잘볼수 있음", "다른교수님들은 마이웨이인 교수님들이 많은데 모두다 이해시키려고 노력하시는 스타일이라서 좋았음 교수님이 설명해주시는거 따라가면서 천천히 이해하면 성적 잘나옴\n", 20240010L, 4.0, "소프트웨어공학", 1L, 0L, 1L)
//        );
//
//    }
//
//
//
//}
