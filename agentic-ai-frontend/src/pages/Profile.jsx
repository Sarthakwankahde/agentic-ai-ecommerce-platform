// src/pages/Profile.jsx

import { useEffect, useState } from "react";

import {
    getProfile,
    updateProfile,
    changePassword
} from "../services/profileService";

function Profile() {

    const [profile, setProfile] = useState(null);

    const [fullName, setFullName] = useState("");

    const [currentPassword, setCurrentPassword] =
        useState("");

    const [newPassword, setNewPassword] =
        useState("");

    const [confirmPassword, setConfirmPassword] =
        useState("");

    const [loading, setLoading] =
        useState(true);

    const [message, setMessage] =
        useState("");

    const [error, setError] =
        useState("");


    // ========================================
    // LOAD PROFILE
    // ========================================

    const loadProfile = async () => {

        try {

            setLoading(true);
            setError("");

            const data =
                await getProfile();

            setProfile(data);

            setFullName(
                data.fullName
            );

        } catch (error) {

            console.error(
                "Failed to load profile:",
                error
            );

            setError(
                error.response?.data?.message ||
                "Failed to load profile."
            );

        } finally {

            setLoading(false);
        }
    };


    // ========================================
    // INITIAL LOAD
    // ========================================

    useEffect(() => {

        loadProfile();

    }, []);


    // ========================================
    // UPDATE PROFILE
    // ========================================

    const handleUpdateProfile = async (event) => {

        event.preventDefault();

        try {

            setError("");
            setMessage("");

            const data =
                await updateProfile(
                    fullName
                );

            setProfile(data);

            setFullName(
                data.fullName
            );

            setMessage(
                "Profile updated successfully."
            );

        } catch (error) {

            console.error(
                "Failed to update profile:",
                error
            );

            setError(
                error.response?.data?.message ||
                "Failed to update profile."
            );
        }
    };


    // ========================================
    // CHANGE PASSWORD
    // ========================================

    const handleChangePassword = async (event) => {

        event.preventDefault();

        try {

            setError("");
            setMessage("");

            const response =
                await changePassword(
                    currentPassword,
                    newPassword,
                    confirmPassword
                );

            setMessage(response);

            setCurrentPassword("");
            setNewPassword("");
            setConfirmPassword("");

        } catch (error) {

            console.error(
                "Failed to change password:",
                error
            );

            setError(
                error.response?.data?.message ||
                error.response?.data ||
                "Failed to change password."
            );
        }
    };


    // ========================================
    // LOADING
    // ========================================

    if (loading) {

        return (
            <div>

                <h1>
                    Profile
                </h1>

                <p>
                    Loading profile...
                </p>

            </div>
        );
    }


    // ========================================
    // UI
    // ========================================

    return (

        <div className="profile-page">

            <h1>
                My Profile
            </h1>


            {/* ========================================
                SUCCESS MESSAGE
            ======================================== */}

            {message && (

                <p>
                    {message}
                </p>

            )}


            {/* ========================================
                ERROR MESSAGE
            ======================================== */}

            {error && (

                <p>
                    {error}
                </p>

            )}


            {/* ========================================
                PROFILE INFORMATION
            ======================================== */}

            {profile && (

                <div>

                    <h2>
                        Profile Information
                    </h2>

                    <p>
                        ID: {profile.id}
                    </p>

                    <p>
                        Full Name: {profile.fullName}
                    </p>

                    <p>
                        Email: {profile.email}
                    </p>

                    <p>
                        Role: {profile.role}
                    </p>

                </div>
            )}


            {/* ========================================
                UPDATE PROFILE
            ======================================== */}

            <div>

                <h2>
                    Update Profile
                </h2>

                <form
                    onSubmit={
                        handleUpdateProfile
                    }
                >

                    <div>

                        <label>
                            Full Name
                        </label>

                        <input
                            type="text"
                            value={fullName}
                            onChange={(event) =>
                                setFullName(
                                    event.target.value
                                )
                            }
                            required
                        />

                    </div>


                    <button type="submit">
                        Update Profile
                    </button>

                </form>

            </div>


            {/* ========================================
                CHANGE PASSWORD
            ======================================== */}

            <div>

                <h2>
                    Change Password
                </h2>

                <form
                    onSubmit={
                        handleChangePassword
                    }
                >

                    <div>

                        <label>
                            Current Password
                        </label>

                        <input
                            type="password"
                            value={currentPassword}
                            onChange={(event) =>
                                setCurrentPassword(
                                    event.target.value
                                )
                            }
                            required
                        />

                    </div>


                    <div>

                        <label>
                            New Password
                        </label>

                        <input
                            type="password"
                            value={newPassword}
                            onChange={(event) =>
                                setNewPassword(
                                    event.target.value
                                )
                            }
                            required
                        />

                    </div>


                    <div>

                        <label>
                            Confirm Password
                        </label>

                        <input
                            type="password"
                            value={confirmPassword}
                            onChange={(event) =>
                                setConfirmPassword(
                                    event.target.value
                                )
                            }
                            required
                        />

                    </div>


                    <button type="submit">
                        Change Password
                    </button>

                </form>

            </div>

        </div>
    );
}

export default Profile;