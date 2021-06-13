--liquibase formatted sql

--changeset jhipster:20210420185302_add_mandature
alter table appartenance_commission_permanente add mandature_id bigint;
alter table autre_mandat add mandature_id bigint null;
alter table commission_thematique add mandature_id bigint null;
alter table fonction_commission_permanente add mandature_id bigint null;
alter table fonction_executive add mandature_id bigint null;
alter table groupe_politique add mandature_id bigint null;
alter table hemicycle_plan add mandature_id bigint null;
alter table reunion_cao add mandature_id bigint null;
alter table reunion_commission_thematique add mandature_id bigint null;
alter table seance add mandature_id bigint null;

create index appartenance_commission_permanente_mandature_id_idx on appartenance_commission_permanente (mandature_id);
create index autre_mandat_mandature_id_idx on autre_mandat (mandature_id);
create index commission_thematique_mandature_id_idx on commission_thematique (mandature_id);
create index fonction_commission_permanente_mandature_id_idx on fonction_commission_permanente (mandature_id);
create index fonction_executive_mandature_id_idx on fonction_executive (mandature_id);
create index groupe_politique_mandature_id_idx on groupe_politique (mandature_id);
create index hemicycle_plan_mandature_id_idx on hemicycle_plan (mandature_id);
create index reunion_cao_mandature_id_idx on reunion_cao (mandature_id);
create index reunion_commission_thematique_mandature_id_idx on reunion_commission_thematique (mandature_id);
create index seance_mandature_id_idx on seance (mandature_id);
