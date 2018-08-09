CREATE TABLE public.vct_dae
(
    gid integer NOT NULL DEFAULT nextval('vct_vertex_gid_seq'::regclass),
   id CHAR(64),
   matricola                   CHAR (64),
   modello                     CHAR (64),
   tipo                        CHAR (64),
   alias                       CHAR (64),
   tipologia_struttura         CHAR (64),
   nomesede                    CHAR (64),
   responsabile                CHAR (64),
   ubicazione                  CHAR (64),
   CONSTRAINT pk_vct_dae_gid PRIMARY KEY (gid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


SELECT AddGeometryColumn ('public','vct_dae','shape',4326,'POINT',2);