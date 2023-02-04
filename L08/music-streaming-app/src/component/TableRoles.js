import React from 'react';

export const TableRoles = () => (
                <table>
                    <tbody>
                        <tr>
                            <th>Role</th>
                            <th>See Songs / Artists</th>
                            <th>Manage Playlists</th>
                            <th>Create Songs</th>
                            <th>Delete Songs</th>
                            <th>Manage Artists</th>
                            <th>Gives roles</th>
                        </tr>
                        <tr>
                            <td>Not logged in</td>
                            <td>X</td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Client</td>
                            <td>X</td>
                            <td>X (own)</td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Artist</td>
                            <td>X</td>
                            <td></td>
                            <td>X (own)</td>
                            <td>X (own)</td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Content Manager</td>
                            <td>X</td>
                            <td></td>
                            <td>X</td>
                            <td>X</td>
                            <td>X</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Administrator aplicatie</td>
                            <td>X</td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>X</td>
                        </tr>
                    </tbody>
                </table>
)