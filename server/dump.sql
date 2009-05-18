BEGIN TRANSACTION;
DELETE FROM sqlite_sequence;
INSERT INTO "sqlite_sequence" VALUES('users',8);
INSERT INTO "sqlite_sequence" VALUES('groups',5);
INSERT INTO "sqlite_sequence" VALUES('configurations',1);
CREATE TABLE "users" ("uidnumber" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "uid" VARCHAR(32) NOT NULL, "mail" VARCHAR(64) NOT NULL, "cn" VARCHAR(64) NOT NULL, "userpassword" VARCHAR(128), "created_at" DATETIME, "updated_at" DATETIME);
INSERT INTO "users" VALUES(1,'kristian','k@web.de','kristian','{SSHA}m96gKLCe5GIMmJvpJ+aAOFB6doUtLVN1biBBcHIgMDUgMDI6NDE6MDIgKzA1MzAgMjAwOS0ta3Jpc3RpYW4tLQ==','2009-03-07T20:43:13+05:30','2009-04-05T02:41:02+05:30');
INSERT INTO "users" VALUES(5,'users','users@example.com','users','{SSHA}cVxcnfqOB910l8TNiTPY0fuSoXYtLVNhdCBNYXIgMjggMDE6NTg6NDQgKzA1MzAgMjAwOS0tdXNlcnMtLQ==','2009-03-28T01:58:44+05:30','2009-03-28T01:58:44+05:30');
INSERT INTO "users" VALUES(6,'groups','groups@example.com','groups','{SSHA}ZdC39TLNPW+RdY8VWLjB9htKa8UtLVNhdCBNYXIgMjggMDE6NTk6MjEgKzA1MzAgMjAwOS0tZ3JvdXBzLS0=','2009-03-28T01:59:21+05:30','2009-03-28T01:59:21+05:30');
INSERT INTO "users" VALUES(7,'usermanager','usermanager@example.com','usermanager','{SSHA}PNQqgSV+dveDmoK5tVJNDSfL2OgtLVNhdCBNYXIgMjggMDI6MDA6NTcgKzA1MzAgMjAwOS0tdXNlcm1hbmFnZXItLQ==','2009-03-28T02:00:57+05:30','2009-03-28T02:00:57+05:30');
INSERT INTO "users" VALUES(8,'void','void@example.com','void','{SSHA}zsMdhIE6GHFfKajQuCvseE+2vP4tLVNhdCBNYXIgMjggMDI6MDI6MzIgKzA1MzAgMjAwOS0tdm9pZC0t','2009-03-28T02:02:32+05:30','2009-03-28T02:02:32+05:30');
CREATE TABLE "groups" ("gidnumber" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "cn" VARCHAR(32) NOT NULL, "description" TEXT NOT NULL);
INSERT INTO "groups" VALUES(1,'root','root');
INSERT INTO "groups" VALUES(4,'users','manage users');
INSERT INTO "groups" VALUES(5,'groups','manage groups');
CREATE TABLE "group_users" ("memberuid" VARCHAR(50) NOT NULL, "gidnumber" INTEGER NOT NULL, PRIMARY KEY("memberuid", "gidnumber"));
INSERT INTO "group_users" VALUES('kristian',1);
INSERT INTO "group_users" VALUES('kristian23',1);
INSERT INTO "group_users" VALUES('kristian2',4);
INSERT INTO "group_users" VALUES('users',4);
INSERT INTO "group_users" VALUES('groups',5);
INSERT INTO "group_users" VALUES('usermanager',4);
INSERT INTO "group_users" VALUES('usermanager',5);
CREATE TABLE "sessions" ("session_id" VARCHAR(50) NOT NULL, "data" TEXT NOT NULL DEFAULT 'BAh7AA== ', "updated_at" DATETIME, PRIMARY KEY("session_id"));
CREATE TABLE "reset_passwords" ("token" VARCHAR(50) NOT NULL, "ip" VARCHAR(50) NOT NULL, "success_url" VARCHAR(50) NOT NULL, "expired_at" DATETIME NOT NULL, "user_id" INTEGER, PRIMARY KEY("token"));
CREATE TABLE "single_sign_ons" ("token" VARCHAR(64) NOT NULL, "ip" VARCHAR(32) NOT NULL, "expired_at" DATETIME NOT NULL, "one_time" VARCHAR(64), "created_at" DATETIME, "updated_at" DATETIME, "user_id" INTEGER, PRIMARY KEY("token"));
CREATE TABLE "configurations" ("id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "password_length" INTEGER NOT NULL, "session_idle_timeout" INTEGER NOT NULL, "created_at" DATETIME NOT NULL, "updated_at" DATETIME NOT NULL);
INSERT INTO "configurations" VALUES(1,12,15,'2009-04-01T00:00:00+05:30','2009-04-05T02:56:07+05:30');
CREATE INDEX "index_users_unique" ON "users" ("uid", "mail");
COMMIT;