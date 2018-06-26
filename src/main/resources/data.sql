-- INSERT INTO public.category (type, id, name, next_level_category_id, icon_url) VALUES ('EXTENDABLE', 1, 'Public Transport', null, 'static/bus.png');
-- INSERT INTO public.category (type, id, name, next_level_category_id, icon_url) VALUES ('EXTENDABLE', 8, 'Taxi', null, 'static/taxi.png');
-- INSERT INTO public.category (type, id, name, next_level_category_id, icon_url) VALUES ('EXTENDABLE', 2, 'Lviv', 1, 'static/lviv.png');
-- INSERT INTO public.category (type, id, name, next_level_category_id, icon_url) VALUES ('EXTENDABLE', 3, 'Kyiv', 1, 'static/kyiv.png');
-- INSERT INTO public.category (type, id, name, next_level_category_id, icon_url) VALUES ('NON_EXTENDABLE', 4, 'Tram', 2, 'static/tram.png');
-- INSERT INTO public.category (type, id, name, next_level_category_id, icon_url) VALUES ('NON_EXTENDABLE', 5, 'Trolleybus', 2, 'static/trolleybus.png');
-- INSERT INTO public.category (type, id, name, next_level_category_id, icon_url) VALUES ('NON_EXTENDABLE', 6, 'Bus', 2, 'static/bigbus.png');
-- INSERT INTO public.category (type, id, name, next_level_category_id, icon_url) VALUES ('NON_EXTENDABLE', 7, 'Marshrutka', 2, 'static/van.png');

-- INSERT INTO public.transit (name, category_id) VALUES ('#9', 4);
-- INSERT INTO public.transit (name, category_id) VALUES ('#2', 5);
-- INSERT INTO public.transit (name, category_id) VALUES ('#3А', 6);
-- INSERT INTO public.transit (name, category_id) VALUES ('#51', 7);
-- INSERT INTO public.transit (name, category_id) VALUES ('#7', 4);
-- INSERT INTO public.transit (name, category_id) VALUES ('#6', 4);

INSERT INTO public.users (email, first_name, last_name, password, role) VALUES ( 'name1@gmail.com', 'Name1', 'Lastname1', '1111',0);
INSERT INTO public.users (email, first_name, last_name, password, role) VALUES ( 'name2@gmail.com', 'Name2', 'Lastname2', '2222',0);
INSERT INTO public.users (email, first_name, last_name, password, role) VALUES ( 'name3@gmail.com', 'Name3', 'Lastname3', '3333',0);ERT INTO public.feedback_criteria (id, type, category_id) VALUES (1, 'RATING', 4);

INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (1, 'RATING', 4);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (2,'RATING', 5);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (3,'RATING', 5);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (4,'RATING', 6);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (5,'RATING', 6);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (6,'RATING', 7);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (7,'RATING', 7);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (8,'ACCEPTER', 4);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (9,'ACCEPTER', 5);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (10,'ACCEPTER', 6);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (11,'ACCEPTER', 7);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (12,'ROUTE_CAPACITY', 5);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (13,'ROUTE_CAPACITY', 6);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (14,'ROUTE_CAPACITY', 7);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (19,'ROUTE_CAPACITY', 4);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (15,'HOURS_CAPACITY', 4);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (16,'HOURS_CAPACITY', 5);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (17,'HOURS_CAPACITY', 6);
INSERT INTO public.feedback_criteria (id, type, category_id) VALUES (18,'HOURS_CAPACITY', 7);


INSERT INTO public.question (id, weight, name, criteria_id) VALUES (1, 1, 'якість проїзду?', 1);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (2, 2, 'якість обслуговуючого персоналу (водій , кондуктор)?', 2);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (3, 3, 'якість проїзду?', 3);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (4, 4, 'в скільки оцінюєте стан тролейбусу?', 4);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (5, 5, 'якість проїзду?', 5);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (6, 6, 'якість салону?', 6);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (7, 7, 'якість проїзду?', 7);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (8, 8, 'оцініть частоту появи маршруток на рейсі?', 8);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (9, 9, 'Чи задоволені тримваєм?', 9);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (10, 10, 'Чи задоволені тролейбусом?', 10);
INSERT INTO public.question (id,weight, name, criteria_id) VALUES (11, 11, 'Чи задоволені автобусом?', 11);
INSERT INTO public.question (id,weight, name, criteria_id) VALUES (12,12, 'Чи задоволені маршруткою?', 12);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (13, 13, 'о котрій годині виїхали?', 13);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (14, 13, 'о котрій годині вийшли?', 13);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (15, 13, 'вкажіть початкову зупинку', 13);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (16, 13, 'вкажіть кінцеву зупинку', 13);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (17, 14, 'о котрій годині виїхали?', 14);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (18, 14, 'о котрій годині вийшли?', 14);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (19, 14, 'вкажіть початкову зупинку', 14);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (20, 14, 'вкажіть кінцеву зупинку', 14);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (21, 15, 'о котрій годині виїхали?', 15);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (22, 15, 'о котрій годині вийшли?', 15);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (23, 15, 'вкажіть початкову зупинку', 15);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (24, 15, 'вкажіть кінцеву зупинку', 15);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (25,16, 'о котрій годині виїхали?', 16);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (26, 16, 'о котрій годині вийшли?', 16);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (27,16, 'вкажіть початкову зупинку', 16);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (28,16, 'вкажіть кінцеву зупинку', 16);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (29, 17, 'о котрій годині виїхали?', 17);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (30,17, 'о котрій годині вийшли?', 17);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (31, 17, 'оцініть завантаженість трамвая', 17);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (32, 18, 'о котрій годині виїхали?', 18);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (33, 18, 'о котрій годині вийшли?', 18);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (34, 18, 'оцініть завантаженість тролейбуса', 18);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (35, 19, 'о котрій годині виїхали?', 19);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (36, 20, 'о котрій годині виїхали?', 20);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (37, 19, 'о котрій годині вийшли?', 19);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (38, 20, 'о котрій годині вийшли?', 20);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (39, 19, 'оцініть завантаженість автобуса', 19);
INSERT INTO public.question (id, weight, name, criteria_id) VALUES (40, 20, 'оцініть завантаженість маршрутки', 20);

INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '10', 1, 4, 1);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '7', 2, 4, 1);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '6', 3, 2, 1);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '2', 4, 2, 1);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '10', 5, 3, 1);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ('9', 6, 3, 1);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ('8', 7, 4, 1);
-- INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '7', 8, 4, 1);
-- INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ('8', 1, 4, 2);
-- INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '6', 2, 4, 2);
-- INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '6', 3, 2, 2);
-- INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ('2', 4, 2, 2);


INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '"YES"', 12, 4, 2);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '"NO"', 12, 4, 1);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '"YES"', 10, 24, 2);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '"YES"', 11, 3, 1);
-- INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '"YES"', 9, 7, 1);
-- INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '"YES"', 10, 2, 3);
-- INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '"NO"', 10, 2, 1);

INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ('{ "capacity":30, "from":{"street":"Автовокзал"} ,"to" :{"street":"вул. Скорини"}}', 13, 3, 3);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '{ "capacity":30, "from":{"street":"Мотозавод"} ,"to" :{"street":"станція Скнилів"}}', 14, 7, 2);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '{ "capacity":10, "from":{"street":"пл. Петрушевича"} ,"to" :{"street":"вул. Зубрівська"}}', 16, 11, 1);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '{ "capacity":30, "from":{"street":"пл. Петрушевича"} ,"to" :{"street":"вул. Сихівська"}}', 16, 24, 3);

INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '{ "startTime" : {"hour":8, "minute":44}, "endTime":{"hour":9, "minute":44} , "capacity":30}', 18, 3, 3);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '{ "startTime" : {"hour":8, "minute":44}, "endTime":{"hour":9, "minute":44} , "capacity":30}', 18, 7, 2);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '{ "startTime" : {"hour":16, "minute":44}, "endTime":{"hour":17, "minute":44} , "capacity":30}', 17, 11, 3);
INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '{ "startTime" : {"hour":2, "minute":44}, "endTime":{"hour":3, "minute":44} , "capacity":30}', 17, 24, 1);

-- INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '[{"weight":1,"answer":4}]', 26, 4, 1);
-- INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '[{"weight":2,"answer":4}]', 21, 2, 1);
-- INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '[{"weight":2,"answer":4}]', 24, 2, 1);
-- INSERT INTO public.feedback ( answer, criteria_id, transit_id, user_id) VALUES ( '[{"weight":4,"answer":5}]', 23, 2, 1);