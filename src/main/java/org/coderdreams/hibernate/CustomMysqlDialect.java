package org.coderdreams.hibernate;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;


public class CustomMysqlDialect extends MySQL8Dialect {
    public CustomMysqlDialect() {
        super();

        registerFunction("levenshtein", new SQLFunctionTemplate(StandardBasicTypes.INTEGER,
                "levenshtein_match_all(?1, ?2, ' ', ?3)"));
    }
}
