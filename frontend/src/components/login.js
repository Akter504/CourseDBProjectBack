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
import {loginUser} from "./auth";
import { useNavigate } from 'react-router-dom';

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

// Стилизация через styled TODO: шаблон
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

export default function Login() {
    const [loginData, setLoginData] = useState({
        email: '',
        passwordHash: '',
    });
    const [message, setMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setLoginData({ ...loginData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsLoading(true);
        setMessage('');
        try {
            const response = await loginUser(loginData);
            if (response.success) {
                console.log(response.token);
                localStorage.setItem('token', response.token);
                setMessage('Успешный вход');
                navigate('/');
            } else {
                setMessage('Неверный email или пароль');
            }
        } catch (error) {
            console.error('Ошибка авторизации', error);
            setMessage('Ошибка авторизации');
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
                    Вход
                </Typography>
                <Form noValidate onSubmit={handleSubmit}>
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        id="email"
                        label="Email"
                        name="email"
                        autoComplete="email"
                        autoFocus
                        value={loginData.email}
                        onChange={handleChange}
                    />
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        name="passwordHash"
                        label="Пароль"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                        value={loginData.passwordHash}
                        onChange={handleChange}
                    />
                    <FormControlLabel
                        control={<Checkbox value="remember" color="primary" />}
                        label="Запомнить меня"
                    />
                    <SubmitButton
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        disabled={isLoading}
                    >
                        {isLoading ? 'Загрузка...' : 'Войти'}
                    </SubmitButton>
                    {message && (
                        <Typography color="error" align="center">
                            {message}
                        </Typography>
                    )}
                    <Box
                        display="grid"
                        gridTemplateColumns="repeat(2, 1fr)"
                        gap={2}
                    >
                        <Box>
                            <Link href="#" variant="body2">
                                Забыли пароль?
                            </Link>
                        </Box>
                        <Box>
                            <Link href="./register" variant="body2">
                                {'Нет аккаунта? Зарегистрироваться'}
                            </Link>
                        </Box>
                    </Box>
                </Form>
            </Paper>
            <Box mt={8}>
                <Copyright />
            </Box>
        </Container>
    );
}
