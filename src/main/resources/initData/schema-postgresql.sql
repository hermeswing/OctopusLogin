DROP TABLE IF EXISTS T_USER_M;

CREATE TABLE IF NOT EXISTS T_USER_M (
    ID SERIAL NOT NULL,
    USER_ID VARCHAR(20) NOT NULL,
    USER_NM VARCHAR(20) NOT NULL,
    PASSWORD VARCHAR(256) NOT NULL,
    EMAIL VARCHAR(200) NOT NULL,
    USER_ROLE VARCHAR(50) NOT NULL,
    CRT_ID VARCHAR(20),
    CRT_DT TIMESTAMP,
    MDF_ID VARCHAR(20),
    MDF_DT TIMESTAMP,
    CONSTRAINT PK_T_USER_M PRIMARY KEY (USER_ID)
);