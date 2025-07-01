# Changelog

## [1.8.0](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.7.0...v1.8.0) (2025-07-01)


### Features

* **swagger:** authorization 추가 ([00edd1d](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/00edd1d9a33144323e85ff890245d692c14685a8)), closes [#51](https://github.com/DDD-Community/DDD-12-GROWIT-BE/issues/51)
* **swagger:** static/docs/index.html ([6299068](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/6299068a1104b06eb0aad2c05d3ada6f51815e41)), closes [#51](https://github.com/DDD-Community/DDD-12-GROWIT-BE/issues/51)


### Bug Fixes

* **review:** PR 요청사항 반영 ([c0e513e](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/c0e513ede19e72fbe093e5c656339c1e7cca8037))

## [1.7.0](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.6.1...v1.7.0) (2025-06-29)


### Features

* **log:** logstash 추가 ([e96dbf8](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/e96dbf8a202233e9db849753ff80e79cee02fe7c))
* **log:** 사용되지 않는 주석 제거 ([60a84f4](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/60a84f4811e8018023fd0d3b0621999ef193b8cf))


### Bug Fixes

* **error:** resource 경로 없는 경우 notFound 에러처리 ([7743d41](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/7743d4121a207132cb52a7267fd542fb03304b0f))
* **error:** resource 경로 없는 경우 notFound 에러처리 ([c4ee233](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/c4ee2336a635facf19ba4baba98745f7e48fcbd0))

## [1.6.1](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.6.0...v1.6.1) (2025-06-29)


### Bug Fixes

* hotfix run ([e92580e](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/e92580e0e9c09d2a96a1d9d232dbd6dd3f4c1cd1))
* hotfix run ([6e7e7cb](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/6e7e7cb00eb1e688420e0b4803a65eb7eda884b3))

## [1.6.0](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.5.0...v1.6.0) (2025-06-29)


### Features

* delete에서 softDelete 변경 ([31f1d7f](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/31f1d7f3a1a70e4daf915f6d5cb3dfddb9761f85))
* delete에서 softDelete 변경 ([010f6dc](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/010f6dcb09b0335cb20c32dba9f2092c3dabe9f4))
* **Fixture:** Goal Fixture, Bulder 추가 ([4ac7beb](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/4ac7bebec29abdb23e6b38caf3ca14acc1ff0be4))
* GoalService ([b967814](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/b967814ca29a25b1d29cb018cae08af49dc959c5))
* GoalService ([75f988c](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/75f988c99f3315799c26932dd1cc5fabe18f85aa))
* **Goal:** 삭제기능 구현 ([195e8ac](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/195e8ac391b1e7686a222e9549e956a223e40980))
* **init:** 목표 삭제 기능 초기 코드 커밋 ([2afdc71](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/2afdc71b2d4177fd231c90138c92d1c15a330e96))


### Bug Fixes

* **DDD:** 도메인 역전 상황 해결 ([61cc5ff](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/61cc5ff63902a48d1d6b19ecd615c33ccc78ac53))
* **Fixture:** SUT 방식으로 변경 ([b9ecb9e](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/b9ecb9ebab79a3406fc735268c6dab649a952cda))
* **Goal:** 삭제 기능 변경 ([0b01e74](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/0b01e74dbc9e6a4122eb970be9adece8e35ce538))
* localDB 변경 ([54fcbf8](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/54fcbf841c1fb310ea8826e583d21f72cbf427f0))
* pr 사항 정리 ([3b1a99c](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/3b1a99c3a158c21bb5f484f66260e73e46f27ac5))
* ResponseMapper 형태 제거 ([a927d51](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/a927d51e8f816f59510482f15a167d09921c3abd))
* 가독성 개선, 변수 개선 ([f7c78ca](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/f7c78cadff54c9de6fdd233a6e35d9b0015d3037))

## [1.5.0](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.4.0...v1.5.0) (2025-06-25)


### Features

* **GetGoalUseCase:** Exception 변경 ([dd8003a](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/dd8003a17ea372dc5f1ae3c63ec8ca799f0ea654))
* **goal:** domain layer goal 객체 추가 ([618ef11](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/618ef11119a6d4eb2ed6a59ffd32812b32a2fe7b)), closes [#26](https://github.com/DDD-Community/DDD-12-GROWIT-BE/issues/26)
* **goal:** GoalEntity userId 추가 ([ea5ee65](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/ea5ee65fc7641316b2c0b2340e25efc883e3f2e3))
* **Goal:** 단일 조회 기능 구현 ([bad705c](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/bad705cbcebd68979151c0fb65a796c46c4f47dc))
* **Goal:** 단일 조회 기능 구현 ([02ff6b2](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/02ff6b2582e41b3515c843a0fb942e3095f8db42))


### Bug Fixes

* **goal:** add endDate mapper ([8610c2e](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/8610c2e864263f348bec81a5d906f5a4fd8d729c)), closes [#26](https://github.com/DDD-Community/DDD-12-GROWIT-BE/issues/26)
* **goal:** Entity 1:N 관계 적용 ([2173715](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/2173715e959b6067aaebe57a06ca4d93c3022e3a))
* **Goal:** goal =&gt; goals ([e1e4aaf](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/e1e4aaf85344125f38025761605df4a91c24c0f6))
* **goal:** setter 제거 ([02756f5](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/02756f5a4de6a75584dc54b00b4a4bc99b6b017f))
* **select 조건 변경:** select 조회 조건 변경 ([8c439c3](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/8c439c316590f4e6f842731e4cd4ca2d03b99398))
* **UseCase:** UserCase 네이밍 변경 및 return 값 변경 ([dc5b6ff](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/dc5b6ff32785d7a4e44f68b74afe1e3f0129c227))
* 미사용 exception 제거 ([67d5e7a](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/67d5e7a57aad8ce1222792067760ade647ec3f37))
* 충돌 병합 ([4516255](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/45162555255672e21fe8a2e94c068def7c1a8761))

## [1.4.0](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.3.1...v1.4.0) (2025-06-22)


### Features

* **logic:** 동의 여부 체크 구현 ([0eb17c3](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/0eb17c3d5a87d2f751656b4ef389ffddc91b77db))
* **logic:** 동의 여부 체크 구현 ([e94c79c](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/e94c79cf7fe249f88c1c33c8fa478092eb363740))

## [1.3.1](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.3.0...v1.3.1) (2025-06-21)


### Bug Fixes

* **release:** openapi3 내용 수정 - 배포용 ([5813e7d](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/5813e7de77d11d3532d5ed17acb52a6d0208669e))

## [1.3.0](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.2.0...v1.3.0) (2025-06-21)


### Features

* **cors:** cors 허용 적용 ([b556396](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/b556396e359d27867d0e5e2fd27e87e06fe0f008))
* **cors:** cors 허용 적용 ([935d607](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/935d6079dc0f2f721365e37a45112e8fca69cdd2))

## [1.2.0](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.1.0...v1.2.0) (2025-06-21)


### Features

* **swagger:** api docs 구현 ([743d89a](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/743d89ac50f0d634f40218d63ce86e1ff2b4c0ff))
* **swagger:** api docs 구현 ([7075b2e](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/7075b2ec2d7cab4970e836c8e798b7f408e3238a)), closes [#16](https://github.com/DDD-Community/DDD-12-GROWIT-BE/issues/16)


### Bug Fixes

* ci error ([8bbb342](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/8bbb34235525be5b9e9ff66f50de743dc1a9774d))
* ci error ([ebad0da](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/ebad0dafa06b1323f51a45be7a87a16622dafe64))

## [1.1.0](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.0.7...v1.1.0) (2025-06-20)


### Features

* **config:** h2 db 연결 ([f123d46](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/f123d46b41c00643c61a53916117be68abc0cb1b))
* **domain:** CareerYear(직무연차) vo 개발 ([ff7f0fe](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/ff7f0fe45bcb5095204af6d23a18969df35bf416))
* **domain:** Email vo 개발 ([5549ab2](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/5549ab29927a04fb79bc8866c126bb075399cc6c))
* **domain:** 로그인관련 도메인 로직 구현 ([34515e6](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/34515e6635c0c54fc13105554c2c8783187370fe))
* **domain:** 직무 리스트 조회 개발 ([0ba3696](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/0ba36961c3d69a41549df784e79a5ea09dfd83b2))
* **jjwt:** 토큰 생성 구현 ([748ef2a](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/748ef2a7ab9c505d10dc0e263ed54941e0333b4e))
* **lib:** using to jnanoid ([3014d03](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/3014d0300920a0a408eb17591a85bbb7a169569a))
* **login:** JWT Token 발급 및 로그인 기능 구현 ([0d4dfa4](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/0d4dfa484d6e4091c21cf8227ec890815ea124c0))
* **reissue:** 토큰 만료시 토큰 재발행 로직 reissue 구현 ([a9078f8](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/a9078f8b1c1be1c1f62dd213c5cc6ce27c9e3d3c))
* **Resource:** JobRole 조회 로직 구현 ([44d6f9e](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/44d6f9e5819c1a90f59ebeabb0a5aebaa621117a))
* **response:** 공통 Entity 생성 ([523169b](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/523169ba60e379b517542c50c3956eb79f50e733))
* **response:** 공통 response, exception 생성 ([f13da4a](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/f13da4adea9434db12a9d5bc4af026b4bc9f0c21))
* **security:** spring security 관련 설정 수정 ([60a9469](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/60a9469faf55389f56dae56ae9129beace1b0ba6))
* **Token:** Token, Jwt 설정, exception 추가 ([3294d54](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/3294d54b3b5d21a5ed48d98761c9f107dae9d5ef))
* **User:** 내정보 조회 ([a7f7d73](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/a7f7d73f8dd60b26af307d62c6827ae20d26bb86))
* **User:** 도메인(회원)과 엔티티(회원) 생성 및 기능 구현 ([c6b17c8](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/c6b17c840923ef8ce97a1ba9f6297f780e088f88))
* **User:** 도메인(회원)과 엔티티(회원) 생성 및 기능 구현 ([1d2974f](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/1d2974f6e3a950a80a1be6260859e1330e12cd04))
* **User:** 서비스 구현 ([f8fba1e](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/f8fba1e3742340d9c334a7e1069c96723e533f82))
* **User:** 회원 가입 기능 구현 ([23f25b8](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/23f25b8b9739ac99696d3ca0306b884f08a994c2))
* **valid:** AuthController Valid 추가 ([4d2a34e](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/4d2a34eae11bb1423f134d4d53f113817260d157))
* **valid:** request valid 메시지 커스텀 ([609125a](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/609125a423866bbc5002da2220c513feea6f6b91))


### Bug Fixes

* **config:** 직무조회 api 제외 ([d1b158a](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/d1b158a56544a12e6815c452ead650ed32b12dd3))
* **ddd:** ddd 방법론에 따른 User.from 생성 ([b0b99a0](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/b0b99a0dd9489ea15aa41afe3c0d12fc77f4d7b6))
* **ddd:** mapper 적용 ([199f840](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/199f840cc8368e26f3e98e3a66df0df0da396c95))
* **deploy:** 배포용 설정으로 변경 ([9ae1057](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/9ae10571464b5395c54cfbef0f45b677d3c2c3cd))
* **deploy:** 배포용 설정으로 변경 ([c2709ea](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/c2709eaa86e310a3c7f82d36218f3fb313ac1f50))
* **folder:** SignUpRequest 위치 조정 ([e8835b9](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/e8835b9d7d8b745d55e8ad7b66774b23ef1415dd))
* **pre-push:** 커밋시 spotless 미동작건 해결 ([16346c6](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/16346c67ebbedb23a4d36b3900fd198ae0a2e69a))
* **security:** Security 경로 허용 ([03b0f05](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/03b0f050e0cf95cdc6ca93d2beb0c9eaa686fa15))
* **valid:** permitAll() /resource/jobroles 추가 ([5289a5b](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/5289a5bd63aad9507212750f1c950e89f6af5d78))

## [1.0.7](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.0.6...v1.0.7) (2025-06-15)


### Bug Fixes

* **cd:** task-def.json 파라미터 위치 변경 ([245d35e](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/245d35ecd5b83490ec12fd4ddcf2ad3fe4f02881))

## [1.0.6](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.0.5...v1.0.6) (2025-06-15)


### Bug Fixes

* **cd:** task-def.json 파라미터 위치 변경 ([38428f9](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/38428f9a4881938a08e6fe47a1a91371918c06a7))

## [1.0.5](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.0.4...v1.0.5) (2025-06-15)


### Bug Fixes

* **cd:** add taskRoleArn ([ed364a5](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/ed364a59fa6d49ec56bee7aadc145378162a95b3))

## [1.0.4](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.0.3...v1.0.4) (2025-06-15)


### Bug Fixes

* **cd:** add "memory": 512 in task-def.json ([ad9d5ec](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/ad9d5ec48858da555c97976b9aac3b3b2a88eba7))

## [1.0.3](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.0.2...v1.0.3) (2025-06-15)


### Bug Fixes

* **cd:** add gradlew build ([28e3880](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/28e3880cdf1152ac825e259879935e7fa2fb9cf4))

## [1.0.2](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.0.1...v1.0.2) (2025-06-15)


### Bug Fixes

* **cd:** Dockerfile path . -&gt; ./app ([36613e3](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/36613e320582d84f4c1d5e2d7e4605844492096f))

## [1.0.1](https://github.com/DDD-Community/DDD-12-GROWIT-BE/compare/v1.0.0...v1.0.1) (2025-06-15)


### Bug Fixes

* **cd:** slack secret 변경 ([34b02da](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/34b02da36d6dc96b00a3d25bcd421ded71d2789c))

## 1.0.0 (2025-06-15)


### Features

* **CI TEST:** CI Test 완료 ([327c5b5](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/327c5b587d407b2a41ce88688c6a56f7048d7979))
* ci/cd 환경변수 수정 ([5193064](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/519306490d5c845214debd1a3ff6346e4ec319a1))
* **인프라 기본 코드 변경:** 배포 세팅 설정 ([b3c9d99](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/b3c9d9964ec567527d17e30f5c8da3db729455ca)), closes [#1](https://github.com/DDD-Community/DDD-12-GROWIT-BE/issues/1)
* 인프라 세팅 및 기본 디렉토리 구조 설계 ([610618c](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/610618c60d4399146b6d2b0ebca5a26eda3e0abe))


### Bug Fixes

* **growit-app-task-def.json:** family, containerDefinitions name ([4c674e9](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/4c674e9d9ecfd081b6d699eab8508e53b7e30cf9))
* release-please 누락 파일 추가 ([f28ed7a](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/f28ed7a94b0ef1ad3e9a4f70783762f9d00c3c7a))
* release-please 누락 파일 추가 ([f28ed7a](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/f28ed7a94b0ef1ad3e9a4f70783762f9d00c3c7a))
* release-please 누락 파일 추가 ([7e73c8f](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/7e73c8fb187f99d386da45ba20716cb13ed9090a))
* **release:** .release-please-manifest.json 추가 ([3da8c7f](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/3da8c7fcf928247f6db11aa7cf23c0b411e54bbe))
* **release:** release.yml v4 로 변경 ([1cb3eab](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/1cb3eabe1988444f549ea5ad51b2d272dbe56050))
* **slack.yml:** ENV 설정 변경 ([4f27c66](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/4f27c66880ec73389b5f97a72305ea16b0078f59))
* **spotlessApply:** spotless 코드 컨벤션 적용 ([600b552](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/600b5522c74460bc3134bb85bae51b20bfdedf9b))
* **test:** secrets 전달 ([e690e5c](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/e690e5cae0bc5ec30ba8262bb4432bda0b166568))
* **test:** secrets 전달 ([c778247](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/c778247e322993ff7c848b99f30910c138187071))
* **test:** slack key 수정 ([8ea4a61](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/8ea4a6151c479c1b658f0e7aae27641c59f0eead))
* **test:** 사용되지 않는 라이브러리 제거 ([577912e](https://github.com/DDD-Community/DDD-12-GROWIT-BE/commit/577912e825ca46104dfc855f3d5076d2019cf04f))
