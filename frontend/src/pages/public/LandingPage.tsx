/**
 * GIRA - Landing Page
 * Public landing page for the application
 */

const LandingPage = () => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center">
      <div className="text-center">
        <h1 className="text-4xl font-bold text-gray-900 mb-4">
          GIRA - Airport Complaint Management System
        </h1>
        <p className="text-lg text-gray-600 mb-8">
          Professional complaint management for airports
        </p>
        <div className="space-x-4">
          <a
            href="/auth/login"
            className="inline-block bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors"
          >
            Login
          </a>
          <a
            href="/auth/register"
            className="inline-block bg-gray-600 text-white px-6 py-3 rounded-lg hover:bg-gray-700 transition-colors"
          >
            Register
          </a>
        </div>
      </div>
    </div>
  );
};

export default LandingPage; 