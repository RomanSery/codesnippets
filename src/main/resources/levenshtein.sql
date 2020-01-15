DELIMITER $$
CREATE FUNCTION `levenshtein`(s1 VARCHAR(255), s2 TEXT ) RETURNS int(11)
    DETERMINISTIC
BEGIN

    DECLARE s1_len, s2_len, i, j, c, c_temp, cost INT;
    DECLARE s1_char CHAR;
-- max strlen=255
    DECLARE cv0, cv1 BLOB;

    SET s1_len = CHAR_LENGTH(s1), s2_len = CHAR_LENGTH(s2), cv1 = 0x00, j = 1, i = 1, c = 0;

    IF s1 = s2 THEN
        RETURN 0;
    ELSEIF s1_len = 0 THEN
        RETURN s2_len;
    ELSEIF s2_len = 0 THEN
        RETURN s1_len;
    ELSE
        WHILE j <= s2_len DO
                SET cv1 = CONCAT(cv1, UNHEX(HEX(j))), j = j + 1;
            END WHILE;
        WHILE i <= s1_len DO
                SET s1_char = SUBSTRING(s1, i, 1), c = i, cv0 = UNHEX(HEX(i)), j = 1;
                WHILE j <= s2_len DO
                        SET c = c + 1;
                        IF s1_char = SUBSTRING(s2, j, 1) THEN
                            SET cost = 0; ELSE SET cost = 1;
                        END IF;
                        SET c_temp = CONV(HEX(SUBSTRING(cv1, j, 1)), 16, 10) + cost;
                        IF c > c_temp THEN SET c = c_temp; END IF;
                        SET c_temp = CONV(HEX(SUBSTRING(cv1, j+1, 1)), 16, 10) + 1;
                        IF c > c_temp THEN
                            SET c = c_temp;
                        END IF;
                        SET cv0 = CONCAT(cv0, UNHEX(HEX(c))), j = j + 1;
                    END WHILE;
                SET cv1 = cv0, i = i + 1;
            END WHILE;
    END IF;
    RETURN c;

END$$
DELIMITER ;



DELIMITER $$
CREATE FUNCTION `levenshtein_match`(
    needle    varchar(255),
    haystack  TEXT,
    splitChar varchar(1),
    maxDistance int
) RETURNS tinyint(4)
    DETERMINISTIC
BEGIN

    declare spacePos int;
    declare searchLen int default 0;
    declare tempStr TEXT default haystack;
    declare tmp TEXT default '';
    declare soundx2 int(11) default 100;

    set searchLen = length(haystack);
    set spacePos = locate(splitChar, tempStr);

    while searchLen > 0 do
            if spacePos = 0
            then
                set tmp = tempStr;
                if length(tmp) <= 3
                then
                    return 100;
                end if;

                select levenshtein(needle, tmp)
                into soundx2;
                if soundx2 <= maxDistance
                then
                    return soundx2;
                else
                    return 100;
                end if;
            else
                set tmp = substr(tempStr, 1, spacePos - 1);
                if length(tmp) <= 3
                then
                    return 100;
                end if;

                set soundx2 = levenshtein(needle, tmp);
                if soundx2 <= maxDistance
                then
                    return soundx2;
                end if;

                set tempStr = substr(tempStr, spacePos + 1);
                set searchLen = length(tempStr);
            end if;

            set spacePos = locate(splitChar, tempStr);
        end while;

    return 100;

END$$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION `levenshtein_match_all`(
    needle    varchar(128),
    haystack  TEXT,
    splitChar varchar(1),
    maxDistance int
) RETURNS tinyint(4)
    DETERMINISTIC
begin
    /* find the first instance of the splitting character */
    declare comma int default 0;
    declare tmpResult int default 0;
    declare word text;

    set comma = LOCATE(splitChar, needle);
    set word = TRIM(needle);

    if LENGTH(haystack) = 0
    then
        return 100;
    elseif comma = 0
    then
        /* one word search term */
        return levenshtein_match(word, haystack, splitChar, maxDistance);
    end if;

    set word = trim(substr(needle, 1, comma));

    /* Insert each split variable into the word variable */
    repeat
        set tmpResult = levenshtein_match(word, haystack, splitChar, maxDistance);
        if tmpResult <= maxDistance
        then
            return tmpResult;
        end if;

        /* get the next word */
        set needle = trim(substr(needle, comma));
        set comma = LOCATE(splitChar, needle);
        if comma = 0
        then
            /* last word */
            return levenshtein_match(needle, haystack, splitChar, maxDistance);
        end if;

        set word = trim(substr(needle, 1, comma));
    until length(word) = 0
        end repeat;

    return 100;
end$$
DELIMITER ;
