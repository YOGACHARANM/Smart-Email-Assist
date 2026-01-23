import { useState } from 'react'
import './App.css'
import { TextField, Typography, Container, Box, InputLabel, MenuItem,FormControl,Select, Button, CircularProgress } from '@mui/material';
import axios from "axios";

function App() {
    const [emailContent, setEmailContent] = useState('');
const [tone, setTone] = useState('');
const [generatedReply, setGeneratedReply] = useState('');
const [loading, setLoading] = useState(false);
const [error, setError] = useState('');

const handlesubmit=async () => {
     setLoading(true);
     setError('');
     try{
    const response = await axios.post("http://localhost:8080/api/email/generate",{
      emailContent,
      tone
    });
    setGeneratedReply(typeof response.data === 'string' ?  response.data : JSON.stringify(response.data));
     }catch(error)
     {
      setError('failed to generate reply/.please try again.')
      console.error(error)
     }finally{
      setLoading(false);
         }

};
  return (
  <Container maxWidth="md" sx={{py:4}}>
    <Typography variant='h3' component='h1' gutterBottom>
      Email Reply Generator
    </Typography>

    <Box sx={{ mx:3 }}>
<TextField 
    fullWidth
    multiline
    rows={6}
    variant='outlined'
    label="original email content "
    value={emailContent ||''}
    onChange={(e)  => setEmailContent(e.target.value)}
    sx={{ mb:2}}
/>  

<FormControl fullWidth sx={{ mb:2}}>
   <InputLabel> Tone (Optional) </InputLabel>
   <Select
   value={tone || ''}
   label={"Tone (Optional)"}
   onChange={(e) => setTone(e.target.value)}>
     <MenuItem value=""> None </MenuItem>
     <MenuItem value="professional">Professional</MenuItem>
     <MenuItem value="casual">Casual</MenuItem>
     <MenuItem value="friendly">Friendly</MenuItem>     
   </Select>
</FormControl>
<Button
  variant='contained'
  onClick={handlesubmit}
  disabled={!emailContent || loading}
    fullWidth>
  {loading ? <CircularProgress size={24} /> : "Generate reply"}
</Button>

  </Box>
{error && (
  <Typography color="error" sx={{ mt: 2 }}>
    {error}
  </Typography>
)}


  {generatedReply && ( 
    <Box sx={{ mt:3}}>
      <Typography variant='h6'  gutterBottom>
      Generated Reply:
    </Typography>
    <TextField
      fullWidth
      multiline
       rows={6}
       variant='outlined'
       value={generatedReply || ''}      
       inputProps={ {readonly:true}}/>  

       <Button
        variant='outlined'
        sx={{  mt:5 }}
        onClick={() => navigator.clipboard.writeText(generatedReply)}>   
        copy the clip   
          </Button>
       
    </Box>
  )}

  
  </Container>
   )
}
export default App
