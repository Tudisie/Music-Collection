import { useState} from "react";
import { useNavigate } from "react-router-dom";
import { getSOAPRegisterBody } from "../utils/SOAPResponses";
import './Register.css';

function Register({handleTokenChange, token}){

    const [username, setUsername] = useState();
    const [password, setPassword] = useState();
    const [repassword, setRePassword] = useState();
    const [email, setEmail] = useState();
    const [name, setName] = useState();
    const [age, setAge] = useState();
    const [error, setError] = useState(false);
    const navigate = useNavigate();

    function validateEmail(mail) {
        if (/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/.test(mail)){
            return (true)
        }
        return (false)
    }

    const handleSubmit = async e => {
        e.preventDefault();

        let err_sync = "";
        if(username === undefined || password === undefined || repassword === undefined){
            setError("Please fill all the required fields!")
        }else{
            email && !validateEmail(email) && (err_sync = "Invalid email!");
            (password !== repassword) && (err_sync = "Passwords don't match!");
            (password.length < 5) && (err_sync = "Password too short!");
            (username.length < 5) && (err_sync = "Username too short!");
        }

        setError(err_sync);
        if(err_sync === ""){
            var sr = getSOAPRegisterBody(username, password, email, name, age);

            var xhr = new XMLHttpRequest();
            var url = "http://localhost:8000";

            xhr.open('POST', url);
            xhr.onreadystatechange = function(){
                if (xhr.readyState === 4) {
                    if(xhr.status === 200)
                    {
                        var XMLParser = require('react-xml-parser');
                        var xml = new XMLParser().parseFromString(xhr.response);
                        const res = xml.getElementsByTagName('tns:full_nameResult');
                        navigate('/login');
                    }
                    else
                    {
                        setError("A unique field does already exist!");
                    }
                }
            }
            xhr.send(sr);
        }
    }

    return(
        <div className="Register">
            <h1>Register</h1>
            <form onSubmit={handleSubmit}>
                <div className="row">
                    <div className="col-35">
                        <label htmlFor="usrname">Username:</label>
                    </div>
                    <div className="col-65">
                        <input type="text" id="usrname" name="username" placeholder="Your username.."
                            onChange={e => setUsername(e.target.value)}/>
                    </div>
                </div>

                <div className="row">
                    <div className="col-35">
                        <label htmlFor="passwd">Password:</label>
                    </div>
                    <div className="col-65">
                        <input type="password" id="passwd" name="password" placeholder="Your password.."
                            onChange={e => setPassword(e.target.value)}/>
                    </div>
                </div>

                <div className="row">
                    <div className="col-35">
                        <label htmlFor="repasswd">Re-enter password:</label>
                    </div>
                    <div className="col-65">
                        <input type="password" id="repasswd" name="repassword" placeholder="Repeat password.."
                            onChange={e => setRePassword(e.target.value)}/>
                    </div>
                </div>

                <div className="row">
                    <div className="col-35">
                        <label htmlFor="x1">Email:</label>
                    </div>
                    <div className="col-65">
                        <input type="text" id="x1" name="username" placeholder="Your email.."
                            onChange={e => setEmail(e.target.value)}/>
                    </div>
                </div>

                <div className="row">
                    <div className="col-35">
                        <label htmlFor="x2">Full name:</label>
                    </div>
                    <div className="col-65">
                        <input type="text" id="x2" name="username" placeholder="Your name.."
                            onChange={e => setName(e.target.value)}/>
                    </div>
                </div>

                <div className="row">
                    <div className="col-35">
                        <label htmlFor="age">Age:</label>
                    </div>
                    <div className="col-65">
                        <input type="number" id="age" name="username" placeholder="Your age.."
                            onChange={e => setAge(e.target.value)}/>
                    </div>
                </div>
                
                <br />
                <div className="row">
                    <input type="submit" value="Submit" />
                </div>
            </form>

            <div className='error-message'>{error}</div>
            
        </div>
    )
};

export default Register;