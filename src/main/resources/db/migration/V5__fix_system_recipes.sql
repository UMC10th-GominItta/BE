DELETE FROM recipe_logs WHERE recipe_id IN (1, 2, 3, 4, 5);
DELETE FROM recipes WHERE recipe_id IN (1, 2, 3, 4, 5);

INSERT INTO recipes (recipe_id, user_id, title, description, estimated_minutes, created_at, updated_at, is_deleted) VALUES
(1,  NULL, '심호흡 5번 하기',           '눈을 감고 코로 깊게 들이마시고 입으로 천천히 내뱉는 심호흡을 5번 반복합니다.', 2,  NOW(), NOW(), false),
(2,  NULL, '스트레칭 하기',             '양손을 깍지 끼고 하늘 위로 기지개를 쭉 켭니다. 천천히 내쉬며 뭉쳐있던 목과 어깨를 부드럽게 돌려주고, 온몸의 근육이 이완되는 감각에 집중해 봅니다.', 5,  NOW(), NOW(), false),
(3,  NULL, '눈 감고 1분 명상',          '화면을 끄고 가만히 눈을 감은 채, 들고나는 숨소리에만 온전히 집중해 봅니다.', 1,  NOW(), NOW(), false),
(4,  NULL, '딱 10초 동안 제자리 털기', '제자리에서 손끝과 발끝, 온몸을 온 힘을 다해 자잘하게 탈탈탈 털어내며 몸의 모든 긴장과 걱정을 날려 보냅니다.', 1,  NOW(), NOW(), false),
(5,  NULL, '오감 그라인딩 실행하기',    '수행 방법: 현재의 감각에 집중히며 마음을 차분히 가라앉히는 5단계 불안 대처 수행법입니다. 현재 눈에 보이는 것 5가지, 촉각 4가지, 소리 3가지, 냄새 2가지, 맛 1가지를 차례로 천천히 찾아보며 현실에 집중해 봅니다.', 5,  NOW(), NOW(), false);
