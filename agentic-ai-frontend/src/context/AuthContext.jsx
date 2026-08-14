import {
    createContext,
    useContext,
    useState
} from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {

    const [isAuthenticated, setIsAuthenticated] = useState(
        Boolean(localStorage.getItem("accessToken"))
    );

    const [user, setUser] = useState(() => {

        const storedUser =
            localStorage.getItem("user");

        return storedUser
            ? JSON.parse(storedUser)
            : null;
    });

    const login = (
        accessToken,
        refreshToken,
        userData = null
    ) => {

        localStorage.setItem(
            "accessToken",
            accessToken
        );

        if (refreshToken) {

            localStorage.setItem(
                "refreshToken",
                refreshToken
            );
        }

        if (userData) {

            localStorage.setItem(
                "user",
                JSON.stringify(userData)
            );

            setUser(userData);
        }

        setIsAuthenticated(true);
    };

    const logout = () => {

        localStorage.removeItem(
            "accessToken"
        );

        localStorage.removeItem(
            "refreshToken"
        );

        localStorage.removeItem(
            "user"
        );

        localStorage.removeItem(
            "email"
        );

        setUser(null);

        setIsAuthenticated(false);
    };

    return (
        <AuthContext.Provider
            value={{
                isAuthenticated,
                user,
                login,
                logout
            }}
        >
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {

    return useContext(AuthContext);
}