--liquibase formatted sql

--changeset jhipster:20211121185302_clean-elu-demission
alter table elu drop column date_demission;
alter table elu drop column motif_demission;
