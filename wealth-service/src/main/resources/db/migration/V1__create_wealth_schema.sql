CREATE TABLE investment_goals (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR(120) NOT NULL,
    target_amount NUMERIC(19,4) NOT NULL,
    current_amount NUMERIC(19,4) NOT NULL,
    target_date DATE NOT NULL,
    risk_tolerance VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_investment_goals_user_id ON investment_goals (user_id);

CREATE TABLE portfolios (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR(120) NOT NULL,
    total_value NUMERIC(19,4) NOT NULL,
    cash_value NUMERIC(19,4) NOT NULL,
    equity_value NUMERIC(19,4) NOT NULL,
    debt_value NUMERIC(19,4) NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_portfolios_user_id ON portfolios (user_id);

CREATE TABLE holdings (
    id UUID PRIMARY KEY,
    portfolio_id UUID NOT NULL REFERENCES portfolios (id) ON DELETE CASCADE,
    symbol VARCHAR(30) NOT NULL,
    asset_class VARCHAR(30) NOT NULL,
    units NUMERIC(19,6) NOT NULL,
    average_cost NUMERIC(19,6) NOT NULL,
    market_value NUMERIC(19,6) NOT NULL,
    last_updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_holdings_portfolio_id ON holdings (portfolio_id);
