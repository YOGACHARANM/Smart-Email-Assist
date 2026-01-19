import { useState } from 'react'
import './App.css'
import { TextField, Typography, Container, Box, InputLabel, MenuItem,FormControl,Select, Button, CircularProgress } from '@mui/material';

function App() {
    const [emailContent, setEmailContent] = useState('');
const [tone, setTone] = useState('');
const [generatedReply, setGeneratedReply] = useState('');
const [loading, setLoading] = useState(false);
const [error, setError] = useState('');

const handlesubmit=async () => {


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
{error && 
          (<Typography color='error' sx={{ mb:2}}>
      Email Reply Generator
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
       
    </Box>
  )}

  
  </Container>
   )
}
export default App
