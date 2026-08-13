import { useEffect, useState } from "react";

import {
    addAddress,
    getMyAddresses,
    updateAddress,
    deleteAddress,
    setDefaultAddress
} from "../services/addressService";

function Address() {

    const email = localStorage.getItem("email");

    const [addresses, setAddresses] = useState([]);

    const [editingId, setEditingId] = useState(null);

    const [loading, setLoading] = useState(true);

    const [message, setMessage] = useState("");

    const [error, setError] = useState("");

    const [form, setForm] = useState({

        fullName: "",
        mobileNumber: "",
        addressLine1: "",
        addressLine2: "",
        city: "",
        state: "",
        country: "",
        pincode: ""

    });


    // ========================================
    // LOAD ADDRESSES
    // ========================================

    const loadAddresses = async () => {

        if (!email) {

            setError(
                "User email not found."
            );

            setLoading(false);

            return;
        }

        try {

            setLoading(true);
            setError("");

            const data =
                await getMyAddresses(email);

            setAddresses(data);

        } catch (error) {

            console.error(
                "Failed to load addresses:",
                error
            );

            setError(
                "Failed to load addresses."
            );

        } finally {

            setLoading(false);
        }
    };


    // ========================================
    // INITIAL LOAD
    // ========================================

    useEffect(() => {

        loadAddresses();

    }, []);


    // ========================================
    // HANDLE INPUT
    // ========================================

    const handleChange = (event) => {

        const { name, value } =
            event.target;

        setForm((previous) => ({

            ...previous,

            [name]: value

        }));
    };


    // ========================================
    // RESET FORM
    // ========================================

    const resetForm = () => {

        setForm({

            fullName: "",
            mobileNumber: "",
            addressLine1: "",
            addressLine2: "",
            city: "",
            state: "",
            country: "",
            pincode: ""

        });

        setEditingId(null);
    };


    // ========================================
    // SUBMIT ADDRESS
    // ========================================

    const handleSubmit = async (event) => {

        event.preventDefault();

        try {

            setError("");
            setMessage("");

            if (editingId) {

                await updateAddress(
                    editingId,
                    email,
                    form
                );

                setMessage(
                    "Address updated successfully."
                );

            } else {

                await addAddress(
                    email,
                    form
                );

                setMessage(
                    "Address added successfully."
                );
            }

            resetForm();

            await loadAddresses();

        } catch (error) {

            console.error(
                "Failed to save address:",
                error
            );

            setError(
                error.response?.data ||
                "Failed to save address."
            );
        }
    };


    // ========================================
    // EDIT ADDRESS
    // ========================================

    const handleEdit = (address) => {

        setEditingId(address.id);

        setForm({

            fullName: address.fullName,

            mobileNumber:
            address.mobileNumber,

            addressLine1:
            address.addressLine1,

            addressLine2:
                address.addressLine2 || "",

            city: address.city,

            state: address.state,

            country: address.country,

            pincode: address.pincode

        });

        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });
    };


    // ========================================
    // DELETE ADDRESS
    // ========================================

    const handleDelete = async (addressId) => {

        const confirmed =
            window.confirm(
                "Are you sure you want to delete this address?"
            );

        if (!confirmed) {
            return;
        }

        try {

            setError("");
            setMessage("");

            await deleteAddress(
                addressId,
                email
            );

            setMessage(
                "Address deleted successfully."
            );

            await loadAddresses();

        } catch (error) {

            console.error(
                "Failed to delete address:",
                error
            );

            setError(
                "Failed to delete address."
            );
        }
    };


    // ========================================
    // SET DEFAULT
    // ========================================

    const handleSetDefault = async (addressId) => {

        try {

            setError("");
            setMessage("");

            await setDefaultAddress(
                addressId,
                email
            );

            setMessage(
                "Default address updated successfully."
            );

            await loadAddresses();

        } catch (error) {

            console.error(
                "Failed to set default address:",
                error
            );

            setError(
                "Failed to set default address."
            );
        }
    };


    // ========================================
    // LOADING
    // ========================================

    if (loading) {

        return (
            <div>

                <h1>My Addresses</h1>

                <p>
                    Loading addresses...
                </p>

            </div>
        );
    }


    // ========================================
    // UI
    // ========================================

    return (

        <div className="address-page">

            <h1>
                My Addresses
            </h1>


            {error && (
                <p>
                    {error}
                </p>
            )}


            {message && (
                <p>
                    {message}
                </p>
            )}


            {/* ========================================
                ADD / UPDATE ADDRESS FORM
            ======================================== */}

            <div>

                <h2>
                    {editingId
                        ? "Update Address"
                        : "Add New Address"}
                </h2>

                <form
                    onSubmit={handleSubmit}
                >

                    <div>

                        <label>
                            Full Name
                        </label>

                        <input
                            type="text"
                            name="fullName"
                            value={form.fullName}
                            onChange={handleChange}
                            required
                        />

                    </div>


                    <div>

                        <label>
                            Mobile Number
                        </label>

                        <input
                            type="text"
                            name="mobileNumber"
                            value={
                                form.mobileNumber
                            }
                            onChange={handleChange}
                            required
                        />

                    </div>


                    <div>

                        <label>
                            Address Line 1
                        </label>

                        <input
                            type="text"
                            name="addressLine1"
                            value={
                                form.addressLine1
                            }
                            onChange={handleChange}
                            required
                        />

                    </div>


                    <div>

                        <label>
                            Address Line 2
                        </label>

                        <input
                            type="text"
                            name="addressLine2"
                            value={
                                form.addressLine2
                            }
                            onChange={handleChange}
                        />

                    </div>


                    <div>

                        <label>
                            City
                        </label>

                        <input
                            type="text"
                            name="city"
                            value={form.city}
                            onChange={handleChange}
                            required
                        />

                    </div>


                    <div>

                        <label>
                            State
                        </label>

                        <input
                            type="text"
                            name="state"
                            value={form.state}
                            onChange={handleChange}
                            required
                        />

                    </div>


                    <div>

                        <label>
                            Country
                        </label>

                        <input
                            type="text"
                            name="country"
                            value={form.country}
                            onChange={handleChange}
                            required
                        />

                    </div>


                    <div>

                        <label>
                            Pincode
                        </label>

                        <input
                            type="text"
                            name="pincode"
                            value={form.pincode}
                            onChange={handleChange}
                            required
                        />

                    </div>


                    <button type="submit">

                        {editingId
                            ? "Update Address"
                            : "Add Address"}

                    </button>


                    {editingId && (

                        <button
                            type="button"
                            onClick={resetForm}
                        >
                            Cancel Edit
                        </button>

                    )}

                </form>

            </div>


            {/* ========================================
                ADDRESS LIST
            ======================================== */}

            <div>

                <h2>
                    Saved Addresses
                </h2>


                {addresses.length === 0 ? (

                    <p>
                        No addresses found.
                    </p>

                ) : (

                    addresses.map((address) => (

                        <div
                            key={address.id}
                            className="address-card"
                        >

                            <h3>
                                {address.fullName}
                            </h3>

                            <p>
                                Mobile:{" "}
                                {address.mobileNumber}
                            </p>

                            <p>
                                {address.addressLine1}
                            </p>

                            {address.addressLine2 && (

                                <p>
                                    {address.addressLine2}
                                </p>

                            )}

                            <p>
                                {address.city},{" "}
                                {address.state}
                            </p>

                            <p>
                                {address.country} -{" "}
                                {address.pincode}
                            </p>


                            {address.isDefault && (

                                <strong>
                                    Default Address
                                </strong>

                            )}


                            <div>

                                <button
                                    onClick={() =>
                                        handleEdit(
                                            address
                                        )
                                    }
                                >
                                    Edit
                                </button>


                                <button
                                    onClick={() =>
                                        handleDelete(
                                            address.id
                                        )
                                    }
                                >
                                    Delete
                                </button>


                                {!address.isDefault && (

                                    <button
                                        onClick={() =>
                                            handleSetDefault(
                                                address.id
                                            )
                                        }
                                    >
                                        Set as Default
                                    </button>

                                )}

                            </div>

                        </div>

                    ))

                )}

            </div>

        </div>
    );
}

export default Address;