import { useEffect, useState } from "react";
import axios from "axios";

// const API_URL = "https://jsonplaceholder.typicode.com/users";
const API_URL = "http://localhost:8081/products/api/v1/products";


// Types
interface User {
    id?: number;
    name: string;
    email: string;
}

const CrudApp = () => {
    // 🔧 Bien typer les states
    const [users, setUsers] = useState<User[]>([]);
    const [newUser, setNewUser] = useState<User>({ name: "", email: "" });
    const [editingUser, setEditingUser] = useState<User | null>(null);

    // Read
    const fetchUsers = async () => {
        try {
            const response = await axios.get<User[]>(API_URL);
            setUsers(response.data);
        } catch (error) {
            console.error("Error fetching users:", error);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    // Create
    const addUser = async () => {
        if (!newUser.name || !newUser.email) {
            alert("All fields are required");
            return;
        }
        try {
            const response = await axios.post<User>(API_URL, newUser);
            setUsers((prev) => [...prev, response.data]);
            setNewUser({ name: "", email: "" });
        } catch (error) {
            console.error("Error adding user:", error);
        }
    };

    // Update
    const updateUser = async () => {
        if (!editingUser?.id) return;
        try {
            await axios.put<User>(`${API_URL}/${editingUser.id}`, editingUser);
            setUsers((prev) =>
                prev.map((u) => (u.id === editingUser.id ? editingUser : u))
            );
            setEditingUser(null);
        } catch (error) {
            console.error("Error updating user:", error);
        }
    };

    // Delete
    const deleteUser = async (id: number) => {
        try {
            await axios.delete(`${API_URL}/${id}`);
            setUsers((prev) => prev.filter((u) => u.id !== id));
        } catch (error) {
            console.error("Error deleting user:", error);
        }
    };

    return (
        <div style={{marginLeft: "5%", marginRight: "5%"}}>

            <p className="text-center">ADJODA-DEV CRUD APP</p>

            <div style={{margin: "2%"}} />

            {/* Add User */}
            <div>
                <input
                    type="text"
                    className="bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md px-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
                    placeholder="Name"
                    value={newUser.name}
                    onChange={(e) => setNewUser({...newUser, name: e.target.value})}
                />
                <input
                    type="email"
                    className="bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md px-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
                    placeholder="Email"
                    value={newUser.email}
                    onChange={(e) => setNewUser({...newUser, email: e.target.value})}
                />
                <button type={"button"}
                        className="rounded-md border border-slate-300 py-2 px-4 text-center text-sm transition-all shadow-sm hover:shadow-lg text-slate-600 hover:text-white hover:bg-slate-800 hover:border-slate-800 focus:text-white focus:bg-slate-800 focus:border-slate-800 active:border-slate-800 active:text-white active:bg-slate-800 disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"
                        onClick={addUser}>
                    Add User
                </button>
            </div>

            <div style={{margin: "2%"}} />

            {/* Search */}
            <div className="w-full ">
                <div className="relative">
                    <input
                        className="w-full bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md pl-3 pr-28 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
                        placeholder="Search ..."
                    />
                    <button
                        className="absolute top-1 right-1 flex items-center rounded bg-slate-800 py-1 px-2.5 border border-transparent text-center text-sm text-white transition-all shadow-sm hover:shadow focus:bg-slate-700 focus:shadow-none active:bg-slate-700 hover:bg-slate-700 active:shadow-none disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"
                        type="button"
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor"
                             className="w-4 h-4 mr-2">
                            <path fillRule="evenodd"
                                  d="M10.5 3.75a6.75 6.75 0 1 0 0 13.5 6.75 6.75 0 0 0 0-13.5ZM2.25 10.5a8.25 8.25 0 1 1 14.59 5.28l4.69 4.69a.75.75 0 1 1-1.06 1.06l-4.69-4.69A8.25 8.25 0 0 1 2.25 10.5Z"
                                  clipRule="evenodd"/>
                        </svg>

                        Search
                    </button>
                </div>
            </div>
            <div style={{margin: "2%"}} />

            {/* Edit User */}
            {editingUser && (
                <div style={{margin: "2%"}}>
                    <input
                        type="text"
                        className="bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md px-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
                        placeholder="Edit Name"
                        value={editingUser.name}
                        onChange={(e) =>
                            setEditingUser((prev) =>
                                prev ? {...prev, name: e.target.value} : prev
                            )
                        }
                    />
                    <input
                        type="email"
                        className="bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md px-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
                        placeholder="Edit Email"
                        value={editingUser.email}
                        onChange={(e) =>
                            setEditingUser((prev) =>
                                prev ? {...prev, email: e.target.value} : prev
                            )
                        }
                    />
                    <button
                        onClick={updateUser}
                        className="rounded-md border border-slate-300 py-2 px-4 text-center text-sm transition-all shadow-sm hover:shadow-lg text-slate-600 hover:text-white hover:bg-slate-800 hover:border-slate-800 focus:text-white focus:bg-slate-800 focus:border-slate-800 active:border-slate-800 active:text-white active:bg-slate-800 disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"

                    >
                        Update
                    </button>

                    <button
                        onClick={() => setEditingUser(null)}
                        className="rounded-md border border-slate-300 py-2 px-4 text-center text-sm transition-all shadow-sm hover:shadow-lg text-slate-600 hover:text-white hover:bg-slate-800 hover:border-slate-800 focus:text-white focus:bg-slate-800 focus:border-slate-800 active:border-slate-800 active:text-white active:bg-slate-800 disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"

                    >
                        Cancel
                    </button>
                </div>
            )}

            {/* List */}


            <div className="relative flex flex-col w-full text-gray-700 bg-white shadow-md rounded-xl bg-clip-border">
                <table className="w-full text-left table-auto min-w-max">
                    <thead>
                    <tr>
                        <th className="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                            <p className="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">
                                Name
                            </p>
                        </th>
                        <th className="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                            <p className="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">
                                Email
                            </p>
                        </th>
                        <th className="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                            <p className="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">
                                Actions
                            </p>
                        </th>
                    </tr>
                    </thead>

                    <tbody>
                    {users.map((user, idx) => (
                        <tr key={user.id ?? `tmp-${idx}`}>
                            <td className="p-4 border-b border-blue-gray-50">
                                <p className="block font-sans text-sm antialiased font-normal leading-normal text-blue-gray-900">
                                    {user.name}
                                </p>
                            </td>
                            <td className="p-4 border-b border-blue-gray-50">
                                <p className="block font-sans text-sm antialiased font-normal leading-normal text-blue-gray-900">
                                    {user.email}
                                </p>
                            </td>
                            <td className="p-4 border-b border-blue-gray-50">
                                <div className="flex items-center gap-3">
                                    <button
                                        onClick={() => setEditingUser(user)}
                                        className="px-3 py-1 rounded-lg text-sm font-medium hover:underline"
                                    >
                                        Edit
                                    </button>
                                    {user.id && (
                                        <button
                                            onClick={() => deleteUser(user.id!)}
                                            className="px-3 py-1 rounded-lg text-sm font-medium hover:underline"
                                        >
                                            Delete
                                        </button>
                                    )}
                                </div>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>


        </div>
    );
};

export default CrudApp;
