ALTER TABLE  DAE_RUOLO
 ADD (VISIBLE  NUMBER);
 
 update DAE_RUOLO set visible = 1;
 
 commit;