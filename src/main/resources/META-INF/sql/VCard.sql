#getAll
SELECT vca_id, vca_name, vca_company_name, vca_company_url, vca_email, vca_voice, vca_cellphone, vca_fax, vca_job_title FROM {schema}.vcard ORDER BY 1 ASC;

#getByEntityId
SELECT vca.vca_id, vca.vca_name, vca.vca_company_name, vca.vca_company_url, vca.vca_email, vca.vca_voice, vca.vca_cellphone, vca.vca_fax, vca.vca_job_title FROM {schema}.vcard vca JOIN {schema}.entity_contact eco ON eco.vca_id = vca.vca_id WHERE eco.ent_id = ?;

#getContactUrisByVcardId
SELECT vcu_uri FROM {schema}.vcard_contact_uri  WHERE vca_id = ? ORDER BY vcu_order;
