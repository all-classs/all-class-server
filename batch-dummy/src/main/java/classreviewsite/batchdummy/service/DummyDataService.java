package classreviewsite.batchdummy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import classreviewsite.batchdummy.model.DummyReviewData;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.service.LectureDataService;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.review.infrastructure.ClassReviewDataRepository;
import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import java.util.Arrays;
import java.util.List;

/**
 * 더미데이터 주입 비즈니스 로직 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DummyDataService {

    private final LectureDataService lectureDataService;
    private final UserService userService;
    private final ClassReviewDataRepository classReviewDataRepository;
    private final EntityManager em;

    /**
     * 데이터베이스가 준비되었는지 확인 (최대 30초 대기)
     */
    public boolean waitForDatabaseReady() {
        int maxRetries = 30; // 30초 대기
        int retryCount = 0;
        
        while (retryCount < maxRetries) {
            try {
                // 기본 테이블들이 존재하고 데이터가 있는지 확인
                lectureDataService.findByUniversity("동서대학교");
                log.info("데이터베이스 준비 완료 확인됨");
                return true;
            } catch (Exception e) {
                retryCount++;
                log.info("데이터베이스 준비 대기 중... ({}/{}초)", retryCount, maxRetries);
                try {
                    Thread.sleep(1000); // 1초 대기
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        
        log.warn("데이터베이스 준비 대기 시간 초과");
        return false;
    }

    /**
     * 더미데이터가 이미 존재하는지 확인
     */
    public boolean isDummyDataAlreadyExists() {
        try {
            // 더미데이터 특정 리뷰가 있는지 확인 (첫 번째 더미데이터의 제목으로 확인)
            Lecture lecture = lectureDataService.findByLectureId(310479L); // 자료구조 강의
            List<ClassReview> reviews = classReviewDataRepository.findAllByLecIdOrderByCreatedDateDesc(lecture);
            
            boolean hasDummyData = reviews.stream()
                .anyMatch(review -> "자료구조".equals(review.getPostTitle()));
            
            if (hasDummyData) {
                log.info("더미데이터가 이미 존재합니다 (총 {}개 리뷰)", reviews.size());
                return true;
            }
            
            return false;
        } catch (Exception e) {
            log.debug("더미데이터 존재 여부 확인 실패: {}", e.getMessage());
            return false; // 에러 시 주입 시도
        }
    }

    /**
     * 더미 수강후기 데이터 주입
     */
    @Transactional
    public void loadDummyReviews() {
        List<DummyReviewData> dummyData = getDummyReviewData();
        
        int successCount = 0;
        int failCount = 0;
        
        for (DummyReviewData data : dummyData) {
            try {
                // lectureId로 강의명 찾기
                Lecture lecture = lectureDataService.findByLectureId(data.getLecId());
                String lectureName = lecture.getLectureName();
                
                ClassReviewRequest request = ClassReviewRequest.of(
                    data.getPostTitle(),
                    data.getPostContent(),
                    data.getUserNumber(),
                    data.getStarRating(),
                    lectureName
                );
                
                // 더미데이터 전용 주입 메소드 사용
                addReviewPostForDummy(request);
                successCount++;
                
                if (successCount % 10 == 0) {
                    log.info("{}건의 더미 리뷰 주입 완료", successCount);
                }
                
            } catch (Exception e) {
                log.warn("리뷰 주입 실패 - userNumber: {}, lecId: {}, 오류: {}", 
                    data.getUserNumber(), data.getLecId(), e.getMessage());
                failCount++;
            }
        }
        
        log.info("더미데이터 주입 완료 - 성공: {}건, 실패: {}건", successCount, failCount);
    }

    /**
     * 더미데이터 주입용 메소드 - 검증 로직 우회
     */
    @Transactional
    public void addReviewPostForDummy(ClassReviewRequest request){
        try {
            Lecture foundLecture = lectureDataService.findByLectureName(request.getLectureName());
            User foundUser = userService.get(request.getUserNumber());

            ClassReview classReview = ClassReview.create(
                    foundLecture,
                    foundUser,
                    request.getStarLating(),
                    0,
                    request.getPostContent(),
                    request.getPostTitle()
            );

            // 수강여부, 중복작성 검증 건너뛰기
            foundLecture.addReview(request.getStarLating()); // 평점 계산은 유지
            classReviewDataRepository.save(classReview);

            em.flush();
            em.clear();
        } catch (Exception e) {
            // 개별 실패는 로그만 남기고 전체 프로세스 중단하지 않음
            throw e;
        }
    }

    /**
     * 더미 수강후기 데이터 목록
     */
    public List<DummyReviewData> getDummyReviewData() {
        return Arrays.asList(
            new DummyReviewData(0, 4.5, 20230857L, 310479L, "자료구조에서 핵심적인 스택,큐,트리, 선형구조 등 이론적인 부분을 수업시간에 배우고 이걸 같은 분반의 팀원들과 함께 수도코드로 공부해보면서 중간고사와 기말 고사를 오랄테스트를 통해 자료구조 및 알고리즘을 좀 더 쉽게 이해하면서 공부를 할수있었다.", "자료구조"),
            new DummyReviewData(0, 2.5, 20230857L, 310480L, "소프트웨어개발실습1을 통해 배웠던 기본적인 내용을 바탕으로 웹 프레임워크를 활용하여 사용자와 상호작용할 수 있는 동적인 UI를 개발하게 해주는 언어을 1학기동안 실습을 통헤 배울수있게된다. 또한 과제를 통해 react로 내가 만들고 싶은 사이트를 만들어 볼수 있는 경험을 할수있는 좋은 기회가 되는 수업이라고 생각한다.", "리액트"),
            new DummyReviewData(0, 0.5, 20220788L, 120006L, "이거 듣지마세요.", "별로임"),
            new DummyReviewData(0, 4.0, 20220788L, 323208L, "실습을 통해 터미널을 제어하고, 가상 머신을 다뤄보며, 은행가 알고리즘 문제를 해결하는 과정 등 학생들과 소통하며 수업이 진행되어 이해하기 더욱 수월했다. 교수님께서 설명을 잘 해주셔서 운영체제의 개념을 쉽게 받아들일 수 있었다. 다양한 알고리즘을 배우면서 마치 운영체제 속으로 직접 들어간 듯한 몰입감을 느낄 수 있었다.", "운영체제, 직접 경험하며 배우다"),
            new DummyReviewData(0, 5.0, 20220788L, 323217L, "데이터베이스 도사님이다", "분위기짱짱맨"),
            new DummyReviewData(0, 4.0, 20220788L, 323207L, "React와 Node.js의 실무적인 코드를 배울 수 있어 만족스럽다. 또한, 포트폴리오에 대한 조언도 함께 들을 수 있어 큰 도움이 되었다. 다만, 이 교수님을 더 일찍 만나지 못한 것이 아쉬울 뿐이다.", "React+node.js의 세상"),
            new DummyReviewData(1, 5.0, 20240841L, 310476L, "교수님이 친절하시고 잘 알려주셔서 이해가 잘 됩니다.\n오픈북 시험으로 진행돼서 시험에 부담이 크지 않아서 좋아요.", "교수님이 좋아요"),
            new DummyReviewData(0, 3.5, 20240841L, 160023L, "교수님이 수업을 재밌게해주셔서 집중이 잘 되는데, 쉬는 시간이 고정되어 있지 않아서 힘듭니다. 앞으론 쉬는 시간을 꼭 지켜주시면 더 좋을 것 같습니다..", "재밌습니다"),
            new DummyReviewData(0, 4.0, 20240841L, 160039L, "교수님이 너무너무너무 친절하시고 잘 알려주세요. 시험에 나오는 것도 다 집어주셔서 부담이 덜한데, 수업이 아주 약간 지루해요...", "수업 좋아요"),
            new DummyReviewData(0, 4.0, 20230919L, 322660L, "수업 중 사담이 조금 많으나 ㅎㅎ 분명히 들어야 할 과목입니다. 수업 시간 강의 내용을 100% 출제하는 것이 원칙이며, 이외에 문제 출제 시 사전에 예상 문제를 집어주셔서 다른 과목들보다 공부하기 수월합니다. 그리고 발표 기회가 있다면 꼭 하는 걸 추천!", "좋은 강의입니다."),
            new DummyReviewData(0, 5.0, 20220788L, 324576L, "수업 자체 분위기가 좋네요. 데이터셋 가지고 학습시키는데 재밌었던것 같습니다. 또한 교수님께서 연산식 이런 부분은 설명을 이해하기 쉽게 해주셔서 듣기 좋았습니다.", "꿀수업 꼭 들으세요!!!!!!"),
            new DummyReviewData(0, 5.0, 20220788L, 323216L, "팀 프로젝트를 하면서 협업 능력도 기를 수 있고, StarUML을 통해 직접 실습도 하면서 다이어그램을 이해할 수 있었던 것 같음. 아이디어를 내는 과정도 매우 즐거웠던 수업. 중간 기말은 필기 시험 쳤어요.", "소프트웨어학과의 꽃인 수업이라 생각함"),
            new DummyReviewData(0, 4.0, 20240787L, 160023L, "앞으로 학교에서 배울것들을 미리 알려주셔서 도움이되었다.", "중요했다"),
            new DummyReviewData(0, 5.0, 20240787L, 160039L, "파이썬에 대해서 기초들을 배울수있었다", "파이썬"),
            new DummyReviewData(0, 5.0, 20240787L, 310476L, "프로그래밍할때 필요한 수학을 배울수있었디.", "선형대수"),
            new DummyReviewData(0, 5.0, 20230919L, 310479L, "개인적으로 테스티벌 방식이 굉장히 흥미롭고 재미있었습니다 알고리즘에 대한 이해도가 굉장히 상승했습니다 ㅎㅎ 단!!! 미루지 말고 바로바로 시험 보시길,,,", "꼭 들어야 하는 수업"),
            new DummyReviewData(0, 5.0, 20230919L, 322658L, "빡센만큼 과목의 이해도가 다른 과목들에 비해 월등히 좋아지고 진짜 공부가 되는 느낌,,, 믿고 듣습니다 조멘", "이 교수님 수업은 무조건 들어야 됨"),
            new DummyReviewData(0, 5.0, 20230919L, 322659L, "수업이 굉장히 빠르고 빡셈 근데 교수님께서는 모르심 너무 능력자셔서 그런가..ㅋ 학생들 대부분이 수업 따라가기 힘들어했음 피그마에 대한 이해도가 높아져 만족스러웠지만 사전 지식이 있다면 어떨까 하는 아쉬움이 있음 참고로 everyday 풀강", "사전 지식이 좀 있어야 할 듯"),
            new DummyReviewData(0, 3.0, 20230919L, 310480L, "설명이 잘 와닿지는 않습니다.. 개강 전 커리큘럼을 확인하고 미리 공부하는 걸 추천", "음... 애매합니다"),
            new DummyReviewData(0, 4.5, 20230857L, 322658L, "컴퓨터 구조는 말 그대로 컴퓨터의 내부 부품들이 어떻게 상호작용하는지 배우게된다. 또한 하드웨어적인 부분에서 어떤 부품들이 무엇에 사용되는지, 우리가 당연하게 사용하고있던 기능들이 내부적으로 어떻게 처리를 하는지 배우는 수업이다. 그래서 하드웨어 관련한 용어들, 개념들 각 레지스터들과 메모리 cpu가 무엇을 수행하고 어떻게 데이터를 처리하는 작업수행 순서도를 배우면서 컴퓨터구조에 대해 알수있는 있는 수업이다.", "컴퓨터구조"),
            new DummyReviewData(0, 4.5, 20230857L, 322659L, "사용자인터페이스기획및설계 수업은 UI/UX를 배우게된다.\n이론적인 내용이 좀 방대하다고 느껴지는 부분이 있다. 그래서 시험기간에 미리 공부하지 않으면..많이 애를 먹었던 과목이기도 하다. 또한 이 수업은 이론만 하는것이 아니라 피그마를 활용한 실습은 사용자에게 보여지는 부분을 어떻게 디자인하고 어떻게 설계할것인지 앞서 배운 이론을 토대로 실습에 적용해본다. 2학년이라면 반드시 들어봐야하는 수업이다.", "UI/UX"),
            new DummyReviewData(0, 3.0, 20230857L, 322660L, "현재는 너무나도 잘 구축되어져 있는 네트워크를 통해 전화도 하고 문자도 할수있다. 전화,문자와 같은 데이터들이 네트워크를 통해 통신할수있다. 이 수업은 이런 통신과 관련해서 네트워크들이 어떻게 이루어져있으며 어떤식으로 데이터를 주고 받는지 배우는 과목이다.", "네트워크"),
            new DummyReviewData(0, 2.0, 20230857L, 322661L, "소프트웨어실무영어라고 하지만 사실상 그냥 대학 영어수업이다. 주제가 소프트웨어관련한 영어단어와 여러 상황별 문장을 통해 영어를 배울수있는 과목이긴하다. 다만 영어를 배우고싶어서 이 과목을 듣는다는것은 별로 추천하지 않는다. 그저 학점을 채우기 위해 수업을 듣는다면 강추한다. 수업자체에 난이도가 있는 수업은 아니라서 학점 따기는 좋은 수업인듯하다.", "영어"),
            new DummyReviewData(0, 5.0, 20231313L, 310476L, "학생들 개개인마다 잘 따라가고 있는지 확인을 잘 해주시고 수업도 학생들 수준에 맞추어 잘 설명해주셔서 좋았습니다!", "수업 후기"),
            new DummyReviewData(0, 5.0, 20231313L, 322658L, "학생들 눈높이 위주로 수업해주시고 한명한명 잘 챙겨주십니다\n수업 내용 외에도 학교생활이나 추가 정보들이 있으면 다 알려주시고 학생 한명 한명 신경써주셔서 너무 좋았습니다. 수업 내용도 이해하기 쉽게 잘 설명해주십니다", "후기"),
            new DummyReviewData(0, 5.0, 20231313L, 160039L, "이론과 실습을 같이 병행하여 이해하기 조금 더 쉬웠고 학생들의 눈높이에 맞춰 수업을 하셔서 좋았습니다", "후기"),
            new DummyReviewData(0, 5.0, 20231313L, 322659L, "진도를 너무 빨리 나가 따라가기 어려웠고 실습하는건 재밌었습니다", "후기"),
            new DummyReviewData(0, 5.0, 20230862L, 310479L, "자료구조와 해당 자료구조를 통한 삽입, 삭제 연산을 수행하는 알고리즘에 대해 학습할 수 있는 강의이다.\n중간고사와 기말고사가 따로 없고 테스티벌이라는 이름의 테스트를 진행한다.\n테스티벌은 각각의 자료구조(스택, 힙, 트리 등등)에 대해 개념이나 삽입 삭제 등의 연산을 하는 방법을 교수님께 직접 설명하여 합격도장을 받는 방법의 테스트 방식이다.", "자료구조에 대해 학습할 수 있는 강의"),
            new DummyReviewData(1, 5.0, 20230862L, 310480L, "리액트를 맛볼 수 있는 강의이다. 리액트의 기초를 배우고 해당 내용을 바탕으로 간단한 프로젝트를 해볼 수 있다. 프론트엔드에 관심이 많다면 재미있고 의미있는 시간이 될 것이다.", "리액트를 배울 수 있는 좋은 시간"),
            new DummyReviewData(0, 5.0, 20230862L, 322658L, "내용이 많고 쉽지 않아 시간투자가 꽤 많이 필요하지만 아주 중요하다고 생각되는 강의이다.\n아마 소프트웨어학과에서 조대수 교수님을 처음 만나는 강의가 될 것인데, 큰 구조를 설명하시며 그 안의 세부적인 설명으로 들어가기 때문에 강의의 몰입력이 아주 좋고 재미있다.\n무조건 들어야 한다.", "컴퓨터 하드웨어에 대해 배울 수 있는 유일한 강의?"),
            new DummyReviewData(0, 4.5, 20230862L, 322659L, "사용자인터페이스는 디자인이라고 생각한다. 디자인의 범위는 우리가 생각하는것보다 크다는 것을 알 수 있는 시간이다.\n강의의 내용이 절대 쉽지 않지만, UI에 대해 심층적으로 배울 수 있는 강의가 이것 말고는 없는것으로 알기 때문에 듣는것을 추천한다. 피그마에 대해서도 배울 수 있다.", "디자인에 대해 배울 수 있는 시간"),
            new DummyReviewData(0, 3.0, 20231313L, 322660L, "수업이 너무 어려웠어요 교수님은 열심히 수업해주셨는데 이해하기 어려웠어요 ㅠㅠ", "후기"),
            new DummyReviewData(0, 3.5, 20231313L, 160023L, "교수님이 열심히 수업 해주셨는데 내용이 어려웠어요 ㅠㅠ 휴강을 자주 하셔서 좋았어요", "후기"),
            new DummyReviewData(0, 2.5, 20240841L, 120069L, "교수님이 혼자 말하고 혼자 답하는 수업인 것 같아요... 글 써야 하는 시간에 교수님이 계속 설명하셔서 집중이 너무 안 돼요ㅠㅠ", "너무 아쉬워요"),
            new DummyReviewData(0, 4.0, 20201595L, 323207L, "아직 전공 지식이 많이 부족하여도 쉽게 설명을 하셔서 누구나 들을 수 있다", "누구나 할 수있다"),
            new DummyReviewData(0, 1.5, 20201595L, 323208L, "이 과목은 매우 중요한 전공 지식을 알려주지만 그 만큼 외울게 많아 매우 힘든 수업 이라고 할 수 있다.", "암기의 잘 하자"),
            new DummyReviewData(0, 5.0, 20201595L, 323216L, "이 수업을 통하면 uml 등 필요한 실무 능력을 기르는데 큰 도움이 될 수 있다", "매우 필요한 지식+1"),
            new DummyReviewData(0, 5.0, 20201595L, 323217L, "이 과목을 통해서 데이터베이스를 이론으로만 배우지 않고 팀 프로젝트와 개인 프로젝트를 통해서 실무 지식까지 함유 할 수있는 과목임", "동서인이라면 꼭 들어야 하는 과목"),
            new DummyReviewData(0, 3.5, 20201595L, 324692L, "데이터 마이닝이라는 과목 명에 속지 마라 파이썬 기초 배움", "뭐 배우는지 잘 모름")
        );
    }
}