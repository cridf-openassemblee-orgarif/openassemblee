delete from elu_adresse_mail where adresse_mail_id in (select id from adresse_mail where niveau_confidentialite != 'PUBLIABLE');
delete from elu_adresses_postales where adresse_postale_id in (select id from adresse_postale where niveau_confidentialite != 'PUBLIABLE');
delete from elu_numero_fax where numero_fax_id in (select id from numero_fax where niveau_confidentialite != 'PUBLIABLE');
delete from elu_numero_telephone where numero_telephone_id in (select id from numero_telephone where niveau_confidentialite != 'PUBLIABLE');
delete from adresse_mail where niveau_confidentialite != 'PUBLIABLE';
delete from adresse_postale where niveau_confidentialite != 'PUBLIABLE';
delete from numero_fax where niveau_confidentialite != 'PUBLIABLE';
delete from numero_telephone where niveau_confidentialite != 'PUBLIABLE';
