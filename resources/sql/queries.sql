-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO uusers
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)

-- :name update-user! :! :n
-- :doc update an existing user record
UPDATE uusers
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT * FROM uusers
WHERE id = :id

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM uusers
WHERE id = :id