import React, { useState } from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { styled } from '@mui/material/styles';
import {registerUser} from "./auth";
import {useNavigate} from "react-router-dom";

function Copyright() {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {'Copyright © '}
            <Link color="inherit" href="">
                Meow
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

const Paper = styled(Box)(({ theme }) => ({
    marginTop: theme.spacing(8),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
}));

const AvatarStyled = styled(Avatar)(({ theme }) => ({
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
}));

const Form = styled('form')(({ theme }) => ({
    width: '100%',
    marginTop: theme.spacing(1),
}));

const SubmitButton = styled(Button)(({ theme }) => ({
    margin: theme.spacing(3, 0, 2),
}));

export default function Register() {
    const [userData, setUserData] = useState({
        userName: '',
        email: '',
        phoneNumber: '',
        surnameUser: '',
        passwordHash: ''
    });
    const [message, setMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserData({ ...userData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsLoading(true);
        try {
            const response = await registerUser(userData);
            console.log(response);
            if (response.success) {
                localStorage.setItem('token', response.token);
                setMessage("Successful registration");
                navigate('/');
            } else {
                setMessage("The registration was not successful");
            }
        } catch (error) {
            setMessage('An error occurred during registration');
        } finally {
            setIsLoading(false);
        }
    };


    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <Paper>
                <AvatarStyled>
                    <LockOutlinedIcon />
                </AvatarStyled>
                <Typography component="h1" variant="h5">
                    Sign up
                </Typography>
                <Form noValidate onSubmit={handleSubmit}>
                    <Box mb={2} display="flex" flexDirection="row" justifyContent="space-between">
                        <TextField
                            autoComplete="uname"
                            name="userName"
                            variant="outlined"
                            required
                            fullWidth
                            label="Username"
                            autoFocus
                            value={userData.userName}
                            onChange={handleChange}
                        />
                        <TextField
                            variant="outlined"
                            required
                            fullWidth
                            name="surnameUser"
                            label="Surname"
                            autoComplete="sname"
                            value={userData.surnameUser}
                            onChange={handleChange}
                        />
                    </Box>
                    <Box mb={2}>
                        <TextField
                            variant="outlined"
                            required
                            fullWidth
                            name="email"
                            label="Email Address"
                            autoComplete="email"
                            value={userData.email}
                            onChange={handleChange}
                        />
                    </Box>
                    <Box mb={2}>
                        <TextField
                            variant="outlined"
                            required
                            fullWidth
                            name="phoneNumber"
                            label="Phone number"
                            type="number"
                            autoComplete="phone-number"
                            value={userData.phoneNumber}
                            onChange={handleChange}
                        />
                    </Box>
                    <Box mb={2}>
                        <TextField
                            variant="outlined"
                            required
                            fullWidth
                            name="passwordHash"
                            label="Password"
                            type="password"
                            autoComplete="current-password"
                            value={userData.passwordHash}
                            onChange={handleChange}
                        />
                    </Box>
                    <Box mb={2}>
                        <FormControlLabel
                            control={<Checkbox value="allowExtraEmails" color="primary" />}
                            label="I want to receive inspiration, marketing promotions and updates via email."
                        />
                    </Box>
                    <SubmitButton
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        disabled={isLoading}
                    >
                        {isLoading ? 'Loading...' : 'Register'}
                    </SubmitButton>
                    {message && (
                        <Typography color="error" align="center">
                            {message}
                        </Typography>
                    )}
                    <Box mt={2} textAlign="center">
                        <Link href="./login" variant="body2">
                            Do you already have an account? Login.
                        </Link>
                    </Box>
                </Form>
            </Paper>
            <Box mt={5}>
                <Copyright />
            </Box>
        </Container>
    );
}