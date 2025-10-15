-- DDL para la tabla USUARIO
CREATE TABLE USUARIO (
                         RUT           VARCHAR2(12)   NOT NULL,          -- clave natural (ej.: RUT)
                         NOMBRE        VARCHAR2(100)  NOT NULL,
                         EMAIL         VARCHAR2(200)  NOT NULL,
                         CUPO_MAX      NUMBER(3)      NOT NULL,
                         CUPO_ACTUAL   NUMBER(3)      DEFAULT 0 NOT NULL,
                         FECHA_CREACION DATE          DEFAULT SYSDATE NOT NULL,
                         CONSTRAINT PK_USUARIO PRIMARY KEY (RUT),
                         CONSTRAINT UQ_USUARIO_EMAIL UNIQUE (EMAIL),
                         CONSTRAINT CK_USUARIO_CUPOS CHECK (CUPO_ACTUAL BETWEEN 0 AND CUPO_MAX)
);