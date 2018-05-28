INSERT INTO public.category (type, id, name, next_level_category_id) VALUES ('EXTENDABLE', 18, 'Public Transport', null);
INSERT INTO public.category (type, id, name, next_level_category_id) VALUES ('EXTENDABLE', 17, 'Lviv', 18);
INSERT INTO public.category (type, id, name, next_level_category_id) VALUES ('NON_EXTENDABLE', 16, 'Tram', 17);
INSERT INTO public.category (type, id, name, next_level_category_id) VALUES ('NON_EXTENDABLE', 19, 'Trolleybus', 17);
INSERT INTO public.category (type, id, name, next_level_category_id) VALUES ('NON_EXTENDABLE', 24, 'Bus', 17);
INSERT INTO public.category (type, id, name, next_level_category_id) VALUES ('NON_EXTENDABLE', 25, 'Marshrutka', 17);
INSERT INTO public.feedback_criteria (id, question, type, category_id, group_id) VALUES (3, 'В які години ви користуєтесь', 'BUSY_HOURS', 18, 2);
INSERT INTO public.feedback_criteria (id, question, type, category_id, group_id) VALUES (1, 'Стан', 'RATING', 18, 1);
INSERT INTO public.feedback_criteria (id, question, type, category_id, group_id) VALUES (2, 'Комфорт', 'RATING', 18, 1);
INSERT INTO public.transit (id, name, category_id) VALUES (1, '#9', 18);
INSERT INTO public.feedback (id, answer, user_id, criteria_id, transit_id) VALUES (1, '59', 777, 1, 1);
INSERT INTO public.feedback (id, answer, user_id, criteria_id, transit_id) VALUES (2, '21', 777, 2, 1);
