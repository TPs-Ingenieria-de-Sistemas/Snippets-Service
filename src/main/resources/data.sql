-- Create formatting rules
INSERT INTO rule (id, default_value, name, rule_type, value_type)
VALUES (1, 'false',
        'hasSpaceBetweenColon',
        'FORMATTING', 'BOOLEAN')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO rule (id,  default_value, name, rule_type, value_type)
VALUES (2, 'false',
        'hasSpaceBetweenEqualSign',
        'FORMATTING', 'BOOLEAN')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO rule (id, default_value, name, rule_type, value_type)
VALUES (3, '1',
        'lineBreakBeforePrintLn',
        'FORMATTING', 'INTEGER')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO rule (id, default_value, name, rule_type, value_type)
VALUES (4, '2',
        'ifBlockIndent',
        'FORMATTING', 'INTEGER')
    ON CONFLICT (id) DO NOTHING;

-- Create linting rules
INSERT INTO rule (id, default_value, name, rule_type, value_type)
VALUES (5,
        '^[a-z]+(?:[A-Z][a-z]*)*$', 'variableNamingRule',
        'LINTING', 'STRING')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO rule (id, default_value, name, rule_type, value_type)
VALUES (6, 'false',
        'printLnArgumentNonExpressionRule',
        'LINTING', 'BOOLEAN')
    ON CONFLICT (id) DO NOTHING;