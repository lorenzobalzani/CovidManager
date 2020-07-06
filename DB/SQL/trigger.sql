USE CovidManager;

/**
  Check if username already exists
 */
DELIMITER $$
create trigger usernameCheck before insert on CREDENZIALI
    for each row
    begin
        if (EXISTS( SELECT *
                    FROM CREDENZIALI
                    WHERE username = NEW.username))
        then
            signal sqlstate '45000'
            set message_text = 'Duplicate username. Please choose
            another';
        end if;
    end; $$

/**
Check in diario clinico if dates are consistent
*/
DELIMITER $$
create trigger dateCheck before insert on DIARIO_CLINICO
    for each row
    begin
        if (new.dataRimozione > NOW() OR new.dataRimozione < new.dataCreazione)
        then
            signal sqlstate '45000'
                set message_text = 'Elimination date cannot be greater than creation date';
        end if;
    end; $$
