module.exports = {
    moduleDirectories: ["node_modules", "src"],
    moduleNameMapper: {
        ".*config.yml$": "<rootDir>/tests/mocks/config.js"
    }
};
