# HoiMC Plugin - 1단계 완료 ✓

Minecraft Paper 서버를 위한 Hearts of Iron 스타일 전략 게임 플러그인

## 📦 1단계: 완료된 기능

### ✅ 구현된 시스템

1. **기본 플러그인 구조**
   - Paper 플러그인 설정 완료
   - Maven 빌드 시스템 구성
   - 패키지 구조 설정

2. **국가 시스템**
   - 6개 국가 구현 (독일, 소련, 미국, 영국, 일본, 한국)
   - 각 국가별 정부 형태, 초기 자원, 버프 설정
   - CountryManager를 통한 국가 관리

3. **국가 선택 시스템**
   - 국가 선택 GUI (인벤토리 기반)
   - 국가 선택북 아이템
   - 플레이어-국가 매핑 시스템

4. **명령어 시스템**
   - `/country select` - 국가 선택 GUI 열기
   - `/country info` - 현재 국가 정보 보기
   - `/country list` - 모든 국가 목록 보기
   - `/country reset` - 국가 선택 초기화 (관리자)
   - `/country givebook` - 선택북 지급 (관리자)

## 🏗 프로젝트 구조

```
hoimc-plugin/
├── src/
│   └── main/
│       ├── java/com/hoimc/
│       │   ├── HoiMCPlugin.java          # 메인 플러그인 클래스
│       │   ├── commands/
│       │   │   └── HoiMCCommand.java     # 명령어 처리
│       │   ├── listeners/
│       │   │   └── PlayerJoinListener.java # 플레이어 이벤트
│       │   ├── items/
│       │   │   └── CountrySelectionBook.java # 국가 선택북
│       │   ├── gui/
│       │   │   └── CountrySelectionGUI.java # 국가 선택 GUI
│       │   ├── managers/
│       │   │   └── CountryManager.java   # 국가 관리
│       │   └── models/
│       │       ├── Country.java          # 국가 데이터 모델
│       │       └── GovernmentType.java   # 정부 형태 Enum
│       └── resources/
│           └── plugin.yml                # 플러그인 설정
└── pom.xml                              # Maven 설정
```

## 🛠 빌드 방법

### 필수 요구사항
- Java 17 이상
- Maven 3.6 이상
- Paper 서버 1.20.4 이상

### 빌드 명령어

```bash
# 프로젝트 디렉토리로 이동
cd hoimc-plugin

# Maven으로 빌드
mvn clean package

# 생성된 JAR 파일 위치
# target/hoimc-plugin-1.0.0-SNAPSHOT.jar
```

## 📥 설치 방법

1. Paper 서버 다운로드 및 설정
2. `hoimc-plugin-1.0.0-SNAPSHOT.jar`를 서버의 `plugins/` 폴더에 복사
3. 서버 시작
4. 플레이어가 접속하면 자동으로 국가 선택북이 지급됨

## 🎮 사용 방법

### 플레이어 명령어

```
/country select    # 국가 선택 GUI 열기
/country info      # 내 국가 정보 보기
/country list      # 모든 국가 목록 보기
```

### 관리자 명령어

```
/country reset     # 국가 선택 초기화
/country givebook  # 국가 선택북 지급
```

## 🌍 구현된 국가

| 국가 | 정부 형태 | 고유 자원 | 특수 버프 |
|------|----------|----------|----------|
| 독일 | 파시즘 | 철 1000, 석탄 800 | 생산속도 +20% |
| 소련 | 공산주의 | 철 900, 석유 700 | 병력 +50% |
| 미국 | 민주주의 | 철 800, 석유 1000 | 연구속도 +30% |
| 영국 | 민주주의 | 철 700, 석탄 600 | 해군력 +40% |
| 일본 | 파시즘 | 철 600, 석유 400 | 군속도 +20% |
| 한국 | 민주주의 | 밀 500, 철 400 | 방어력 +10% |

## 📋 다음 단계 (2단계)

- [ ] 자원 시스템 구현
- [ ] 지역(Region) 시스템 구축
- [ ] ResourceManager 완성
- [ ] 자원 노드 생성 및 자동 생산
- [ ] 자원 UI 패널 구현

## 🧪 테스트 체크리스트

- [x] 플러그인 로딩 확인
- [x] 플레이어 접속 시 선택북 지급
- [x] 국가 선택 GUI 열기
- [x] 국가 선택 기능
- [x] 명령어 작동 확인
- [x] 중복 국가 선택 방지
- [ ] 서버 재시작 후 국가 유지 (영속성)

## 📝 주의사항

- 현재는 메모리에만 데이터를 저장 (서버 재시작 시 초기화)
- 2단계에서 데이터 영속성 기능 추가 예정
- Paper 1.20.4 기준으로 개발됨

## 🤝 기여

이슈나 개선 사항은 GitHub Issues에 등록해주세요.

## 📄 라이선스

MIT License
