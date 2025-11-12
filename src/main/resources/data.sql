-- Seed initial users (safe to run repeatedly)
INSERT INTO public.users (username, email, active)
VALUES ('admin', 'admin@example.com', true)
ON CONFLICT (username) DO NOTHING;

INSERT INTO public.users (username, email, active)
VALUES ('testuser', 'testuser@example.com', true)
ON CONFLICT (username) DO NOTHING;
