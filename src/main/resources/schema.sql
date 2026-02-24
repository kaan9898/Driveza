/**
 * Clear legacy unique constraints on rental.user_id / rental.vehicle_id so
 * that storing historical rentals is possible once the relationship is
 * ManyToOne. The names of those constraints depend on how Hibernate created
 * the schema, so we query pg_constraint/pg_attribute and drop whatever unique
 * constraint touches those columns.
 */
DO $$
DECLARE
    constraint_name text;
BEGIN
    FOR constraint_name IN
        SELECT con.conname
        FROM pg_constraint con
                 JOIN pg_attribute att ON att.attrelid = con.conrelid
                 AND att.attnum = ANY(con.conkey)
        WHERE con.conrelid = 'rental'::regclass
          AND con.contype = 'u'
          AND att.attname IN ('user_id', 'vehicle_id')
    LOOP
        EXECUTE format('ALTER TABLE rental DROP CONSTRAINT IF EXISTS %I', constraint_name);
    END LOOP;
END$$;
