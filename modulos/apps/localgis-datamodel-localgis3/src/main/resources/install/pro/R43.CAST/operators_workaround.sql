CREATE FUNCTION pg_catalog.texteqint(text, integer) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(int4out($2)) = $1;$$;
CREATE FUNCTION pg_catalog.texteqint2(text, smallint) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(int2out($2)) = $1;$$;
CREATE FUNCTION pg_catalog.texteqoid(text, oid) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(oidout($2)) = $1;$$;
CREATE FUNCTION pg_catalog.texteqdate(text, date) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(date_out($2)) = $1;$$;
CREATE FUNCTION pg_catalog.texteqfloat8(text, double precision) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(float8out($2)) = $1;$$;
CREATE FUNCTION pg_catalog.texteqreal(text, real) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(float4out($2)) = $1;$$;
CREATE FUNCTION pg_catalog.texteqtimetz(text, time WITH time zone) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(timetz_out($2)) = $1;$$;
CREATE FUNCTION pg_catalog.texteqtime(text, time without time zone) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(time_out($2)) = $1;$$;
CREATE FUNCTION pg_catalog.texteqtimestamptz(text, timestamp WITH time zone) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(timestamptz_out($2)) = $1;$$;
CREATE FUNCTION pg_catalog.texteqinterval(text, interval) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(interval_out($2)) = $1;$$;
CREATE FUNCTION pg_catalog.texteqint8(text, bigint) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(int8out($2)) = $1;$$;
CREATE FUNCTION pg_catalog.texteqnumeric(text, numeric) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(numeric_out($2)) = $1;$$;
CREATE FUNCTION pg_catalog.texteqtimestamp(text, timestamp without time zone) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(timestamp_out($2)) = $1;$$;
 
CREATE FUNCTION pg_catalog.inteqtext(integer, text) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(int4out($1)) = $2;$$;
CREATE FUNCTION pg_catalog.int2eqtext(smallint, text) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(int2out($1)) = $2;$$;
CREATE FUNCTION pg_catalog.oideqtext(oid, text) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(oidout($1)) = $2;$$;
CREATE FUNCTION pg_catalog.dateeqtext(date, text) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(date_out($1)) = $2;$$;
CREATE FUNCTION pg_catalog.float8eqtext(double precision, text) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(float8out($1)) = $2;$$;
CREATE FUNCTION pg_catalog.realeqtext(real, text) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(float4out($1)) = $2;$$;
CREATE FUNCTION pg_catalog.timetzeqtext(time WITH time zone, text) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(timetz_out($1)) = $2;$$;
CREATE FUNCTION pg_catalog.timeeqtext(time without time zone, text) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(time_out($1)) = $2;$$;
CREATE FUNCTION pg_catalog.timestamptzeqtext(timestamp WITH time zone, text) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(timestamptz_out($1)) = $2;$$;
CREATE FUNCTION pg_catalog.intervaleqtext(interval, text) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(interval_out($1)) = $2;$$;
CREATE FUNCTION pg_catalog.int8eqtext(bigint, text) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(int8out($1)) = $2;$$;
CREATE FUNCTION pg_catalog.numericeqtext(numeric, text) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(numeric_out($1)) = $2;$$;
CREATE FUNCTION pg_catalog.timestampeqtext(timestamp without time zone, text) RETURNS BOOLEAN STRICT IMMUTABLE LANGUAGE SQL AS $$SELECT textin(timestamp_out($1)) = $2;$$;
 
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.texteqint, LEFTARG=text, RIGHTARG=integer, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.texteqint2, LEFTARG=text, RIGHTARG=smallint, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.texteqoid, LEFTARG=text, RIGHTARG=oid, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.texteqdate, LEFTARG=text, RIGHTARG=date, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.texteqfloat8, LEFTARG=text, RIGHTARG=double precision, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.texteqreal, LEFTARG=text, RIGHTARG=real, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.texteqtimetz, LEFTARG=text, RIGHTARG=time WITH time zone, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.texteqtime, LEFTARG=text, RIGHTARG=time without time zone, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.texteqtimestamptz, LEFTARG=text, RIGHTARG=timestamp WITH time zone, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.texteqinterval, LEFTARG=text, RIGHTARG=interval, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.texteqint8, LEFTARG=text, RIGHTARG=bigint, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.texteqnumeric, LEFTARG=text, RIGHTARG=numeric, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.texteqtimestamp, LEFTARG=text, RIGHTARG=timestamp without time zone, COMMUTATOR=OPERATOR(pg_catalog.=));
 
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.inteqtext, LEFTARG=integer, RIGHTARG=text, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.int2eqtext, LEFTARG=smallint, RIGHTARG=text, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.oideqtext, LEFTARG=oid, RIGHTARG=text, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.dateeqtext, LEFTARG=date, RIGHTARG=text, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.float8eqtext, LEFTARG=double precision, RIGHTARG=text, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.realeqtext, LEFTARG=real, RIGHTARG=text, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.timetzeqtext, LEFTARG=time WITH time zone, RIGHTARG=text, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.timeeqtext, LEFTARG=time without time zone, RIGHTARG=text, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.timestamptzeqtext, LEFTARG=timestamp WITH time zone, RIGHTARG=text, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.intervaleqtext, LEFTARG=interval, RIGHTARG=text, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.int8eqtext, LEFTARG=bigint, RIGHTARG=text, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.numericeqtext, LEFTARG=numeric, RIGHTARG=text, COMMUTATOR=OPERATOR(pg_catalog.=));
CREATE OPERATOR pg_catalog.= ( PROCEDURE = pg_catalog.timestampeqtext, LEFTARG=timestamp without time zone, RIGHTARG=text, COMMUTATOR=OPERATOR(pg_catalog.=));
