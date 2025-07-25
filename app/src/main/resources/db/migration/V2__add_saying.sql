
CREATE TABLE sayings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    message    varchar(256) not null,
    "from"     varchar(32)  not null,
    CONSTRAINT pk_sayings PRIMARY KEY (id)
);

INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (1, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '성공은 매일 반복되는 작은 노력들의 합이다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (2, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '실패를 두려워하지 않는 자만이 혁신할 수 있다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (3, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '완벽을 추구하지 말고, 먼저 시작하라냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (4, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '더 나은 답은 항상 질문 속에 숨어 있다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (5, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '속도가 늦어도 멈추지 않는 것이 중요하다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (6, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '집중은 최고의 생산성 도구다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (7, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '작은 개선이 큰 변화를 만든다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (8, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '끊임없는 학습이 나를 업그레이드한다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (9, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '문제를 사랑하라, 그 안에 기회가 있다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (10, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '어제의 나를 이기는 것이 최고의 경쟁이다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (11, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '실행하지 않으면 아무 일도 일어나지 않는다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (12, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '자유롭게 상상하되, 현실적으로 실행하라냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (13, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '실패란 성공의 설계도일 뿐이다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (14, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '결정하지 않으면 아무 것도 이루어지지 않는다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (15, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '에너지와 인내가 결국 승리를 가져온다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (16, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '가장 빠른 길은 꾸준함이다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (17, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '시작이 반이다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (18, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '복잡함 속에서 단순함을 찾아라냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (19, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '불가능은 생각의 벽일 뿐이다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (20, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '항상 ''왜?''를 물어라냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (21, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '최고의 아이디어는 종종 테이블 위의 커피 잔 옆에서 나온다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (22, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '미래를 예측하는 가장 좋은 방법은 만드는 것이다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (23, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '결국 해내는 사람은 계속 도전한 사람이다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (24, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '작게라도 매일 쌓아 올려라냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (25, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '실패는 내가 더 강해질 재료다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (26, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '포기하지 않는 한 끝난 것이 아니다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (27, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '문제를 직면하는 것이 해결의 시작이다냥!', '그로냥', null);
INSERT INTO public.sayings (id, created_at, updated_at, message, "from", deleted_at) VALUES (28, '2025-07-12 15:54:13.562202', '2025-07-12 15:54:13.562202', '나는 늘 부족하다 생각하기에 계속 성장한다냥!', '그로냥', null);
