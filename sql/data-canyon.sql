-- =========================
-- USERS (si necesitas uno básico)
-- =========================

INSERT INTO users (id, username, surname, email, password)
VALUES
    (1, 'test_user', 'Test', 'test1@mail.com', 'password'),
    (2, 'mountain_rider', 'Lopez', 'test2@mail.com', 'password'),
    (3, 'river_explorer', 'Garcia', 'test3@mail.com', 'password');

-- =========================
-- DESCENTS TEST DATA
-- =========================
INSERT INTO descents (
    id,
    user_id,
    name,
    location,
    province,
    vertical_character,
    aquatic_character,
    commitment,
    description_link,
    comments,
    created_at,
    updated_at
) VALUES
      (
          1,
          1,
          'Barranco de la Fou',
          'Tarragona',
          'Catalunya',
          'v1',
          'a1',
          'IV',
          'https://example.com/fou',
          'Descenso técnico con agua abundante en primavera',
          NOW(),
          NOW()
      ),
      (
          2,
          1,
          'Gorgs del Freser',
          'Ripoll',
          'Girona',
          'v4',
          'a3',
          'III',
          'https://example.com/freser',
          'Muy vertical, ideal para nivel avanzado',
          NOW(),
          NOW()
      ),
      (
          3,
          1,
          'Barranco seco de Bierge',
          'Bierge',
          'Huesca',
          'v1',
          'a1',
          'II',
          NULL,
          'Perfecto para iniciación',
          NOW(),
          NOW()
      );