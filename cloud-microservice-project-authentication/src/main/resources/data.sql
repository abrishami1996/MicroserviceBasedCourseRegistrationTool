INSERT IGNORE INTO roles (name) VALUES
  ("ROLE_ADMIN"),
  ("ROLE_STUDENT"),
  ("ROLE_PROFESSOR"),
  ("ROLE_EMPLOYEE");

 INSERT IGNORE INTO user (id,fk,password,username) VALUES
   (1,-1,"$2a$10$Zh75qJ64P9v7lnC3vQ7gv.CVUua1fzHcLJNn4cVGsJOBw4wtdA.RC","admin");

insert IGNORE into user_roles (user_id,role_id) values("1","1");