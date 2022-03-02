<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
</p>

<br>

# 지하철 노선도 미션
## 🚀 1단계 - 경로 조회 타입 추가
**인수 조건**
```
Feature: 지하철 경로 검색

  Scenario: 두 역의 최소 시간 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
    Then 최소 시간 기준 경로를 응답
    And 총 거리와 소요 시간을 함께 응답함
```

**소요 시간 추가**
```
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;
    
    ...
}
```

<br>

## 🚀 2단계 - 요금 조회
**인수 조건**
```
Feature: 지하철 경로 검색

  Scenario: 두 역의 최단 거리 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
    Then 최단 거리 경로를 응답
    And 총 거리와 소요 시간을 함께 응답함
    And 지하철 이용 요금도 함께 응답함
```
**요금 계산 방법**
- [x] 기본운임(10㎞ 이내) : 기본운임 1,250원
- [x] 이용 거리초과 시 추가운임 부과
    * 10km초과∼50km까지(5km마다 100원)
    * 50km초과 시 (8km마다 100원)
    
<br>

## 🚀 3단계 - 요금 정책 추가
### 추가된 요금 정책
**노선별 추가 요금**
- [x] 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
    * ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
    * ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
- [x] 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
    * ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원

**로그인 사용자의 경우 연령별 요금으로 계산**
- [x] 청소년: 운임에서 350원을 공제한 금액의 20%할인
- [x] 어린이: 운임에서 350원을 공제한 금액의 50%할인
