CREATE TABLE member(
    no NUMBER NOT NULL,
    id VARCHAR2(50) NOT NULL,
    passwd VARCHAR2(250) NOT NULL,
    name VARCHAR2(50) NOT NULL,
    gender VARCHAR2(1) NOT NULL CHECK(gender IN ('M', 'F')),
    bornYear NUMBER NOT NULL,
    postcode VARCHAR2(100) NOT NULL,
    address VARCHAR2(100) NOT NULL,
    detailAddress VARCHAR2(100) NOT NULL,
    extraAddress VARCHAR2(100) NULL,
    regiDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    unique(no)
);

CREATE SEQUENCE seq_member START WITH 1 INCREMENT BY 1 MINVALUE 1;

SELECT * FROM member ORDER BY no desc;

select * from member where id = 'hong' and passwd = '1';

UPDATE member SET bornYear = 1000 WHERE no = '1';

commit;

CREATE TABLE memo(
    no NUMBER NOT NULL,
    writer VARCHAR2(50) NOT NULL,
    content VARCHAR2(250) NOT NULL,
    regiDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(no)
);

CREATE SEQUENCE seq_memo START WITH 1 INCREMENT BY 1 MINVALUE 1;

SELECT * FROM memo ORDER BY no;

SELECT COUNT(*) FROM member WHERE id = 'hong';

SELECT * FROM 
    (SELECT ROWNUM Rnum, a.* FROM (SELECT * FROM member WHERE no > 0 ORDER BY no DESC) a) 
WHERE Rnum >= 10 AND Rnum <= 20;





begin
for i in 1 .. 545 loop
insert into member values
(seq_member.nextval, i, i, i, 'M', 1999, 20202, 'a', 'b', 'c', current_Timestamp);
end loop;
commit;
end;
/



CREATE TABLE guestbook(
    no NUMBER NOT NULL,
    name VARCHAR2(50) NOT NULL,
    email VARCHAR2(50) NOT NULL,
    passwd VARCHAR2(50) NOT NULL,
    content CLOB NOT NULL,
    regiDate DATE DEFAULT SYSDATE,
    PRIMARY KEY(no)
);

CREATE SEQUENCE seq_guestbook START WITH 1 INCREMENT BY 1 NOMAXVALUE NOCACHE;

SELECT * FROM guestbook;



CREATE TABLE survey(
    no NUMBER NOT NULL,
    question VARCHAR2(4000) NOT NULL,
    ans1 VARCHAR2(500) NOT NULL,
    ans2 VARCHAR2(500) NOT NULL,
    ans3 VARCHAR2(500) NOT NULL,
    ans4 VARCHAR2(500) NOT NULL,
    status CHAR(1) DEFAULT '1',
    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    regi_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(no)
);

CREATE SEQUENCE seq_survey START WITH 1 INCREMENT BY 1 NOMAXVALUE NOCACHE;


CREATE TABLE survey_answer (
    answer_no NUMBER NOT NULL PRIMARY KEY,
    no NUMBER NOT NULL REFERENCES survey(no),
    answer NUMBER NOT NULL,
    regi_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE seq_survey_answer START WITH 1 INCREMENT BY 1 NOMAXVALUE NOCACHE;

SELECT * FROM survey ORDER BY no;

SELECT * FROM survey_answer ORDER BY no;

DESC survey_answer;


SELECT * FROM survey WHERE no > 0 AND CURRENT_TIMESTAMP > last_date;

SELECT * FROM survey WHERE no > 0 AND (CURRENT_TIMESTAMP BETWEEN start_date AND last_date);


SELECT t1.*, (SELECT COUNT(*) FROM survey_answer t2 WHERE t2.no = t1.no) survey_counter FROM survey t1 WHERE no > 0;



SELECT COUNT(*) FROM survey WHERE no > 0 AND (CURRENT_TIMESTAMP BETWEEN start_date AND last_date) AND question LIKE '%f%';



SELECT * FROM survey_answer WHERE no = 15 ORDER BY answer_no;

SELECT survey.*, (SELECT COUNT(*) FROM survey_answer WHERE survey_answer.no = survey.no) survey_counter FROM survey ORDER BY no DESC;

SELECT survey.*, (SELECT COUNT(*) FROM survey_answer WHERE no = survey.no) survey_counter FROM survey ORDER BY no DESC;

SELECT * FROM survey;
SELECT * FROM survey_answer;

SELECT no, (SELECT COUNT(*) FROM survey_answer WHERE survey_answer.no = survey.no) survey_counter FROM survey ORDER BY no DESC;

SELECT no FROM survey ORDER BY no DESC;

SELECT no, (SELECT COUNT(*) FROM survey_answer WHERE survey.no = no) survey_counter FROM survey ORDER BY no DESC;


SELECT no, 
(SELECT COUNT(answer) FROM survey_answer WHERE no = 15 AND answer = '1') count_of_1, 
(SELECT COUNT(answer) FROM survey_answer WHERE no = 15 AND answer = '2') count_of_2, 
(SELECT COUNT(answer) FROM survey_answer WHERE no = 15 AND answer = '3') count_of_3, 
(SELECT COUNT(answer) FROM survey_answer WHERE no = 15 AND answer = '4') count_of_4 
FROM survey_answer WHERE no = 15 GROUP BY no;


SELECT * FROM v_total_answers ORDER BY no;

SELECT * FROM survey_answer;

SELECT no, answer FROM survey_answer;

SELECT * FROM (SELECT no, answer FROM survey_answer)
PIVOT(COUNT(*) FOR answer IN (1 AS ans1c, 2 AS ans2c, 3 AS ans3c ,4 AS ans4c));


select a.*, (select count(*) from survey_answer where a.no=no) survey_counter, b.ans1c, b.ans2c, b.ans3c, b.ans4c from survey a, v_ansCount b where a.no=b.no;

select count(*) from survey_answer where no = 16;

select 
    survey.*, 
    (select count(*) from survey_answer where survey.no = no) survey_counter, 
    v_ansCount.ans1c, 
    v_ansCount.ans2c, 
    v_ansCount.ans3c, 
    v_ansCount.ans4c 
from 
    survey, 
    v_ansCount 
where 
    survey.no = v_ansCount.no(+);

select rownum rn, tb.* from (select a.no, a.question, a.ans1, a.ans2, a.ans3, a.ans4, a.status, a.start_date, a.last_date, a.regi_date, (select count(*) from survey_answer where a.no=no) survey_counter, b.ans1c, b.ans2c, b.ans3c, b.ans4c from survey a, v_ansCount b where a.no=b.no(+)) tb;


SELECT no, answer FROM survey_answer;

SELECT * FROM (SELECT no, answer FROM survey_answer)
PIVOT (COUNT(*) FOR answer IN (1, 2, 3, 4)) WHERE no = 16;


select (select count(*) from survey_answer where a.no=no) survey_counter, b.ans1c, b.ans2c, b.ans3c, b.ans4c from survey a, v_ansCount b where a.no=b.no;

CREATE OR REPLACE VIEW v_responses_by_question AS
SELECT * FROM (SELECT answer, no FROM survey_answer)
PIVOT(COUNT(*) FOR answer IN (1 AS count_of_1, 2 AS count_of_2, 3 AS count_of_3, 4 AS count_of_4));

SELECT * FROM v_responses_by_question;
SELECT * FROM survey;
SELECT * FROM survey_answer;
SELECT COUNT(*) FROM survey_answer WHERE survey_answer.no = 1;

SELECT * 
FROM 
    (SELECT 
        survey.no, 
        (SELECT COUNT(*) FROM survey_answer WHERE survey_answer.no = survey.no) total_answers, 
        v_responses_by_question.count_of_1, 
        v_responses_by_question.count_of_2, 
        v_responses_by_question.count_of_3, 
        v_responses_by_question.count_of_4 
    FROM 
        survey, 
        v_responses_by_question 
    WHERE survey.no = v_responses_by_question.no(+)) response_result 
WHERE response_result.no = 5;

CREATE TABLE board(
	no NUMBER NOT NULL,
	num NUMBER NOT NULL,
	tbl VARCHAR2(50) NOT NULL,
	writer VARCHAR2(50) NOT NULL,
	subject VARCHAR2(50) NOT NULL,
	content CLOB NOT NULL,
	email VARCHAR2(50) NOT NULL,
	passwd VARCHAR2(50) NOT NULL,
	refNo NUMBER NOT NULL,
	stepNo NUMBER NOT NULL,
	levelNo NUMBER NOT NULL,
	parentNo NUMBER NOT NULL,
	hit NUMBER NOT NULL,
	ip VARCHAR2(50) NOT NULL,
	memberNo NUMBER NOT NULL,
	noticeNo NUMBER NOT NULL,
	secretGubun VARCHAR2(1) NOT NULL CHECK (secretGubun IN ('T', 'F')),
	regiDate DATE DEFAULT SYSDATE,
    PRIMARY KEY(no)
);

CREATE SEQUENCE seq_board START WITH 1 INCREMENT BY 1 NOMAXVALUE NOCACHE;

DESC board;

SELECT * FROM board ORDER BY noticeNo DESC, refNo DESC, levelNo ASC;

SELECT * FROM board ORDER BY noticeNo DESC;

SELECT * 
FROM 
    (SELECT 
        b.*, 
        (SELECT COUNT(*) 
        FROM board 
        WHERE refNo = b.refNo AND stepNo = (b.stepNo + 1) AND levelNo = (b.levelNo + 1)) child_counter, 
        LAG(no) OVER (ORDER BY noticeNo DESC, refNo DESC, levelNo ASC) preNo, 
        LAG(subject) OVER (ORDER BY noticeNo DESC, refNo DESC, levelNo ASC) preSubject, 
        LEAD(no) OVER (ORDER BY noticeNo DESC, refNo DESC, levelNo ASC) nxtNo, 
        LEAD(subject) OVER (ORDER BY noticeNo DESC, refNo DESC, levelNo ASC) nxtSubject 
    FROM board b 
    ORDER BY noticeNo DESC, refNo DESC, levelNo ASC) 
WHERE no = 3;

SELECT COUNT(*) FROM board b WHERE refNo = b.refNo AND stepNo = (b.stepNo + 1) AND levelNo = (b.levelNo + 1);

SELECT noticeNo FROM board WHERE no = 4;

UPDATE board SET noticeNo = (noticeNo - 1) WHERE noticeNo > (SELECT noticeNo FROM board WHERE no = 4);

UPDATE board SET noticeNo = 2 WHERE no = 19;
UPDATE board SET noticeNo = 3 WHERE no = 10;
commit;

CREATE TABLE board_comment (
    comment_no NUMBER NOT NULL PRIMARY KEY,
    board_no NUMBER NOT NULL REFERENCES board(no),
    writer VARCHAR2(50) NOT NULL,
    content CLOB NOT NULL,
    passwd VARCHAR2(50) NOT NULL,
    memberNo NUMBER NOT NULL,
    ip VARCHAR2(50) NOT NULL,
    regiDate DATE DEFAULT SYSDATE
);

CREATE SEQUENCE seq_board_comment START WITH 1 INCREMENT BY 1 NOMAXVALUE NOCACHE;

SELECT * FROM board_comment WHERE board_no = 19 ORDER BY comment_no DESC;

DESC board_comment;

SELECT * FROM board_comment;

CREATE TABLE boardChk (
    no NUMBER NOT NULL PRIMARY KEY,
    tbl VARCHAR2(50) NOT NULL,
    tblName VARCHAR2(50) NOT NULL,
    serviceGubun VARCHAR2(1) NOT NULL CHECK (serviceGubun IN ('T', 'F')),
    regiDate DATE DEFAULT SYSDATE,
    UNIQUE(tbl),
    UNIQUE(tblName)
);
drop table boardChk;
CREATE SEQUENCE seq_boardChk START WITH 1 INCREMENT BY 1 NOMAXVALUE NOCACHE;

INSERT INTO boardChk VALUES (seq_boardChk.NEXTVAL, 'freeboard', '자유게시판', 'T', SYSDATE);
INSERT INTO boardChk VALUES (seq_boardChk.NEXTVAL, 'funnyboard', '유머게시판', 'T', SYSDATE);
INSERT INTO boardChk VALUES (seq_boardChk.NEXTVAL, 'memberboard', '회원전용게시판', 'F', SYSDATE);
INSERT INTO boardChk VALUES (seq_boardChk.NEXTVAL, 'javaboard', '자바게시판', 'T', SYSDATE);

SELECT * FROM boardChk;

SELECT * FROM board ORDER BY noticeNo DESC, refNo DESC, levelNo ASC;

SELECT tblName, serviceGubun FROM boardChk WHERE tbl = 'dsfdfds';

CREATE TABLE product (
    no NUMBER NOT NULL,
    name VARCHAR2(50) NOT NULL,
    price NUMBER DEFAULT 0,
    description CLOB,
    product_img VARCHAR2(1000) NOT NULL,
    regi_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(no)
);

CREATE SEQUENCE seq_product START WITH 1 INCREMENT BY 1 NOMAXVALUE NOCACHE;

DESC product;

SELECT * FROM product WHERE no > 0;

delete from product where no = 2;

drop table product;
drop sequence seq_product;

commit;

CREATE TABLE cart (
    no NUMBER NOT NULL PRIMARY KEY,
    memberNo NUMBER NOT NULL,
    productNo NUMBER NOT NULL,
    amount NUMBER DEFAULT 0,
    regi_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE seq_cart START WITH 1 INCREMENT BY 1 NOMAXVALUE NOCACHE;

ALTER TABLE cart ADD CONSTRAINT fk_cart_memberNo FOREIGN KEY(memberNo) REFERENCES member(no);
ALTER TABLE cart ADD CONSTRAINT fk_cart_productNo FOREIGN KEY(productNo) REFERENCES product(no);

-- ALTER TABLE cart DROP CONSTRAINT fk_cart_memberNo;

DESC cart;
SELECT * FROM cart;
SELECT * FROM product;

SELECT SUM(amount) FROM cart WHERE productNo = 14;

SELECT product.product_img, product.name, product.price, cart.amount, (product.price * cart.amount) buy_money, cart.regi_date
FROM cart LEFT OUTER JOIN product
ON cart.productNo = product.no;

SELECT COUNT(*)
FROM cart LEFT OUTER JOIN product
ON cart.productNo = product.no;

SELECT 
    cart.no cart_no, 
    product.no productNo, 
    product.product_img, 
    product.name, 
    product.price, 
    cart.amount, 
    (product.price * cart.amount) buy_money, 
    cart.regi_date 
FROM cart LEFT OUTER JOIN product ON cart.productNo = product.no ORDER BY cart_no DESC;

SELECT product.*, (SELECT SUM(amount) FROM cart WHERE cart.productNo = product.no) amount FROM product Where product.no = 14;

SELECT p.name product_name, SUM(c.amount * p.price) buy_money FROM cart c INNER JOIN product p ON c.productNo = p.no GROUP BY p.name ORDER BY product_name ASC;